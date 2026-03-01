package com.example.resourceservice.client;

import com.example.resourceservice.dto.CreateSongResponse;
import com.example.resourceservice.dto.SongMetadataRequest;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class SongServiceClient {
  private final RestTemplate restTemplate = new RestTemplate();
  private final DiscoveryClient discoveryClient;

  public SongServiceClient(DiscoveryClient discoveryClient) {
    this.discoveryClient = discoveryClient;
  }

  private String getSongServiceBaseUrl() {
    return discoveryClient.getInstances("song-service").stream()
        .findFirst()
        .map(instance -> instance.getUri().toString())
        .orElseThrow(() -> new IllegalStateException("song-service not found in Eureka"));
  }

  public CreateSongResponse createSongMetadata(SongMetadataRequest request) {
    String url = getSongServiceBaseUrl() + "/songs";
    ResponseEntity<CreateSongResponse> response =
        restTemplate.postForEntity(url, request, CreateSongResponse.class);
    return response.getBody();
  }

  public void deleteSongs(String csvIds) {
    String url = getSongServiceBaseUrl() + "/songs?id=" + csvIds;
    restTemplate.delete(url);
  }
}
