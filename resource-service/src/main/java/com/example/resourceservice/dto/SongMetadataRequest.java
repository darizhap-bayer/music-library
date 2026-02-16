package com.example.resourceservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SongMetadataRequest {
  @NotNull private Long id;

  @NotBlank
  @Size(max = 100)
  private String name;

  @NotBlank
  @Size(max = 100)
  private String artist;

  @NotBlank
  @Size(max = 100)
  private String album;

  @NotBlank
  @Pattern(
      regexp = "^\\d{2}:\\d{2}$",
      message = "Duration must be in mm:ss format with leading zeros")
  private String duration;

  @NotBlank
  @Pattern(
      regexp = "^(19|20)\\d{2}$",
      message = "Year must be in YYYY format between 1900 and 2099")
  private String year;
}
