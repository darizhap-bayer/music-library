package com.example.resourceservice.service;

import java.util.ArrayList;
import java.util.List;

public class CsvParser {
  public static List<Long> parseCsvIds(String csv) {
    if (csv == null || csv.isBlank()) {
      throw new com.example.resourceservice.exception.InvalidRequestException(
          "CSV must be non-empty");
    }
    if (csv.length() > 200) {
      throw new com.example.resourceservice.exception.InvalidRequestException(
          "CSV string is too long: received "
              + csv.length()
              + " characters, maximum allowed is 200");
    }
    String[] parts = csv.split(",");
    List<Long> ids = new ArrayList<>();
    for (String part : parts) {
      String trimmed = part.trim();
      try {
        long id = Long.parseLong(trimmed);
        if (id <= 0) throw new NumberFormatException();
        ids.add(id);
      } catch (NumberFormatException e) {
        throw new com.example.resourceservice.exception.InvalidRequestException(
            "Invalid ID format: '" + trimmed + "'. Only positive integers are allowed");
      }
    }
    return ids;
  }
}
