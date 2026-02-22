package com.example.resourceservice.client;

import com.example.resourceservice.dto.CreateSongResponse;
import com.example.resourceservice.dto.SongMetadataRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class SongServiceClient {
  private final RestTemplate restTemplate = new RestTemplate();

  @Value("${song-service.url}")
  private String songServiceUrl;

  public CreateSongResponse createSongMetadata(SongMetadataRequest request) {
    String url = songServiceUrl + "/songs";
    ResponseEntity<CreateSongResponse> response =
        restTemplate.postForEntity(url, request, CreateSongResponse.class);
    return response.getBody();
  }

  public void deleteSongs(String csvIds) {
    String url = songServiceUrl + "/songs?id=" + csvIds;
    restTemplate.delete(url);
  }
}
