package com.example.songservice.controller;

import com.example.songservice.dto.CreateSongRequest;
import com.example.songservice.dto.CreateSongResponse;
import com.example.songservice.dto.DeleteSongsResponse;
import com.example.songservice.dto.GetSongResponse;
import com.example.songservice.service.SongService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/songs")
public class SongController {
  @Autowired private SongService songService;

  @PostMapping
  public ResponseEntity<CreateSongResponse> createSong(
      @Valid @RequestBody CreateSongRequest request) {
    CreateSongResponse response = songService.createSong(request);
    return ResponseEntity.ok(response);
  }

  @GetMapping("/{id}")
  public ResponseEntity<GetSongResponse> getSongById(@PathVariable("id") String id) {
    var song = songService.getSongById(id);
    GetSongResponse response =
        new GetSongResponse(
            song.getId(),
            song.getName(),
            song.getArtist(),
            song.getAlbum(),
            song.getDuration(),
            song.getYear());
    return ResponseEntity.ok(response);
  }

  @DeleteMapping
  public ResponseEntity<DeleteSongsResponse> deleteSongs(@RequestParam("id") String csvIds) {
    var deleted = songService.deleteSongs(csvIds);
    return ResponseEntity.ok(new DeleteSongsResponse(deleted));
  }
}
