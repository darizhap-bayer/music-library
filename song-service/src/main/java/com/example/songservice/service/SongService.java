package com.example.songservice.service;

import com.example.songservice.dto.CreateSongRequest;
import com.example.songservice.dto.CreateSongResponse;
import com.example.songservice.entity.Song;
import com.example.songservice.repository.SongRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SongService {
  private static final Logger logger = LoggerFactory.getLogger(SongService.class);

  @Transactional
  public java.util.List<Long> deleteSongs(String csvIds) {
    logger.info("Received delete request for Song IDs: {}", csvIds);
    java.util.List<Long> ids = CsvParser.parseCsvIds(csvIds);
    java.util.List<Long> deleted = new java.util.ArrayList<>();
    for (Long id : ids) {
      if (songRepository.existsById(id)) {
        songRepository.deleteById(id);
        deleted.add(id);
        logger.info("Deleted Song entity with ID: {}", id);
      }
    }
    logger.info("Deleted Song IDs: {}", deleted);
    return deleted;
  }

  @Autowired private SongRepository songRepository;

  public Song getSongById(String id) {
    logger.info("Retrieving song metadata for ID: {}", id);
    Long parsedId = validateAndParseId(id);
    return songRepository
        .findById(parsedId)
        .orElseThrow(
            () ->
                new com.example.songservice.exception.SongNotFoundException(
                    "Song metadata for ID=" + parsedId + " not found"));
  }

  private Long validateAndParseId(String id) {
    if (id == null || id.trim().isEmpty()) {
      throw new com.example.songservice.exception.InvalidRequestException(
          "Invalid value '" + id + "' for ID. Must be a positive integer");
    }
    try {
      long parsedId = Long.parseLong(id.trim());
      if (parsedId <= 0) {
        throw new com.example.songservice.exception.InvalidRequestException(
            "Invalid value '" + id + "' for ID. Must be a positive integer");
      }
      return parsedId;
    } catch (NumberFormatException e) {
      throw new com.example.songservice.exception.InvalidRequestException(
          "Invalid value '" + id + "' for ID. Must be a positive integer");
    }
  }

  @Transactional
  public CreateSongResponse createSong(CreateSongRequest request) {
    logger.info("Creating song metadata: {}", request);
    if (songRepository.existsById(request.getId())) {
      throw new com.example.songservice.exception.SongAlreadyExistsException(
          "Metadata for resource ID=" + request.getId() + " already exists");
    }
    Song song =
        new Song(
            request.getId(),
            request.getName(),
            request.getArtist(),
            request.getAlbum(),
            request.getDuration(),
            request.getYear());
    songRepository.save(song);
    logger.info("Song metadata created with ID: {}", song.getId());
    return new CreateSongResponse(song.getId());
  }
}
