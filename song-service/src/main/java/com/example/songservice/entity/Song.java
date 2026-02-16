package com.example.songservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Song {
  @Id private Long id;

  private String name;
  private String artist;
  private String album;
  private String duration;
  private String year;
}
