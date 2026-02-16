package com.example.resourceservice.service;

import org.springframework.stereotype.Component;

@Component
public class DurationConverter {
  public String secondsToMmSs(double seconds) {
    int totalSeconds = (int) Math.floor(seconds);
    int minutes = totalSeconds / 60;
    int secs = totalSeconds % 60;
    return String.format("%02d:%02d", minutes, secs);
  }
}
