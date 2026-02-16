package com.example.resourceservice.service;

import com.example.resourceservice.client.SongServiceClient;
import com.example.resourceservice.dto.SongMetadataRequest;
import com.example.resourceservice.dto.UploadResourceResponse;
import com.example.resourceservice.entity.Resource;
import com.example.resourceservice.exception.InvalidRequestException;
import com.example.resourceservice.repository.ResourceRepository;
import java.io.IOException;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ResourceService {
  private static final Logger logger = LoggerFactory.getLogger(ResourceService.class);

  @Autowired private ResourceRepository resourceRepository;
  @Autowired private MetadataExtractor metadataExtractor;
  @Autowired private DurationConverter durationConverter;
  @Autowired private SongServiceClient songServiceClient;

  @Transactional
  public java.util.List<Long> deleteResources(String csvIds) {
    // Parse and validate CSV
    java.util.List<Long> ids = CsvParser.parseCsvIds(csvIds);
    logger.info("Received delete request for Resource IDs: {}", ids);
    // Call SongServiceClient to delete metadata first
    logger.info("Calling SongServiceClient to delete song metadata for IDs: {}", ids);
    songServiceClient.deleteSongs(csvIds);
    // Delete resources from DB
    java.util.List<Long> deleted = new java.util.ArrayList<>();
    for (Long id : ids) {
      if (resourceRepository.existsById(id)) {
        resourceRepository.deleteById(id);
        deleted.add(id);
        logger.info("Deleted Resource entity with ID: {}", id);
      }
    }
    logger.info("Deleted Resource IDs: {}", deleted);
    return deleted;
  }

  public Resource getResourceById(String id) {
    Long resourceId = validateAndParseId(id);
    return resourceRepository
        .findById(resourceId)
        .orElseThrow(
            () ->
                new com.example.resourceservice.exception.ResourceNotFoundException(
                    "Resource with ID=" + resourceId + " not found"));
  }

  private Long validateAndParseId(String id) {
    if (id == null || id.trim().isEmpty()) {
      throw new InvalidRequestException(
          "Invalid value '" + id + "' for ID. Must be a positive integer");
    }
    try {
      Long parsedId = Long.parseLong(id);
      if (parsedId <= 0) {
        throw new InvalidRequestException(
            "Invalid value '" + id + "' for ID. Must be a positive integer");
      }
      return parsedId;
    } catch (NumberFormatException e) {
      throw new InvalidRequestException(
          "Invalid value '" + id + "' for ID. Must be a positive integer");
    }
  }

  @Transactional
  public UploadResourceResponse uploadResource(byte[] audioData) throws IOException {
    logger.info(
        "Received upload request. File size: {} bytes", audioData != null ? audioData.length : 0);

    Map<String, String> metadata;
    try {
      metadata = metadataExtractor.extractMetadata(audioData);
      logger.info("Extracted metadata: {}", metadata);
    } catch (Exception e) {
      logger.error("Failed to extract metadata from audio data", e);
      throw new InvalidRequestException("Failed to extract metadata: " + e.getMessage());
    }

    Resource resource = new Resource();
    resource.setAudioData(audioData);
    resource = resourceRepository.save(resource);
    logger.info("Saved Resource entity with ID: {}", resource.getId());

    String duration = metadata.get("duration");
    String durationMmSs =
        duration != null ? durationConverter.secondsToMmSs(Double.parseDouble(duration)) : null;
    SongMetadataRequest songRequest =
        new SongMetadataRequest(
            resource.getId(),
            metadata.get("name"),
            metadata.get("artist"),
            metadata.get("album"),
            durationMmSs,
            metadata.get("year"));
    try {
      logger.info(
          "Calling SongServiceClient to create song metadata for Resource ID: {}",
          resource.getId());
      songServiceClient.createSongMetadata(songRequest);
      logger.info("Song metadata created successfully for Resource ID: {}", resource.getId());
    } catch (Exception e) {
      logger.error(
          "Failed to create song metadata for Resource ID: {}. Rolling back Resource.",
          resource.getId(),
          e);
      resourceRepository.deleteById(resource.getId());
      throw new RuntimeException("Failed to save resource: Song Service unavailable");
    }
    return new UploadResourceResponse(resource.getId());
  }
}
