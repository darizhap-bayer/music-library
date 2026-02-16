package com.example.resourceservice.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.apache.tika.Tika;
import org.apache.tika.metadata.Metadata;
import org.springframework.stereotype.Component;

@Component
public class MetadataExtractor {
  private final Tika tika = new Tika();

  public Map<String, String> extractMetadata(byte[] audioData) throws IOException {
    Map<String, String> metadataMap = new HashMap<>();
    Metadata metadata = new Metadata();
    try (ByteArrayInputStream bais = new ByteArrayInputStream(audioData)) {
      tika.parse(bais, metadata);
    }
    metadataMap.put("name", metadata.get("dc:title"));
    metadataMap.put("artist", metadata.get("xmpDM:artist"));
    metadataMap.put("album", metadata.get("xmpDM:album"));
    metadataMap.put("duration", metadata.get("xmpDM:duration")); // in seconds
    metadataMap.put("year", metadata.get("xmpDM:releaseDate"));
    return metadataMap;
  }
}
