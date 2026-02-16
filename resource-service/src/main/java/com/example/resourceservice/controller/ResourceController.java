package com.example.resourceservice.controller;

import com.example.resourceservice.dto.DeleteResourcesResponse;
import com.example.resourceservice.dto.UploadResourceResponse;
import com.example.resourceservice.service.ResourceService;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/resources")
public class ResourceController {
  @Autowired private ResourceService resourceService;

  @PostMapping(consumes = "audio/mpeg", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<UploadResourceResponse> uploadResource(@RequestBody byte[] audioData)
      throws IOException {
    UploadResourceResponse response = resourceService.uploadResource(audioData);
    return ResponseEntity.ok(response);
  }

  @GetMapping(value = "/{id}", produces = "audio/mpeg")
  public ResponseEntity<byte[]> getResourceById(@PathVariable("id") String id) {
    var resource = resourceService.getResourceById(id);
    return ResponseEntity.ok()
        .contentType(MediaType.valueOf("audio/mpeg"))
        .body(resource.getAudioData());
  }

  @DeleteMapping
  public ResponseEntity<DeleteResourcesResponse> deleteResources(
      @RequestParam("id") String csvIds) {
    var deleted = resourceService.deleteResources(csvIds);
    return ResponseEntity.ok(new DeleteResourcesResponse(deleted));
  }
}
