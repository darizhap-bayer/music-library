package com.example.resourceservice.client;

import com.example.resourceservice.dto.CreateSongResponse;
import com.example.resourceservice.dto.SongMetadataRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class SongServiceClient {
  private final RestTemplate restTemplate = new RestTemplate();
  private static final String SONG_SERVICE_URL = "http://localhost:8081/songs";

  public CreateSongResponse createSongMetadata(SongMetadataRequest request) {
    ResponseEntity<CreateSongResponse> response =
        restTemplate.postForEntity(SONG_SERVICE_URL, request, CreateSongResponse.class);
    return response.getBody();
  }

  public void deleteSongs(String csvIds) {
    String url = SONG_SERVICE_URL + "?id=" + csvIds;
    restTemplate.delete(url);
  }
}
