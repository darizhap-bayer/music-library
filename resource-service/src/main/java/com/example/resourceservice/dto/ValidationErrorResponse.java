package com.example.resourceservice.dto;

import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ValidationErrorResponse {
  private String errorMessage;
  private String errorCode;
  private Map<String, String> details;
}
