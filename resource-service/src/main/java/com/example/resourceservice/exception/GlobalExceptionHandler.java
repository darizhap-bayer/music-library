package com.example.resourceservice.exception;

import com.example.resourceservice.dto.ErrorResponse;
import com.example.resourceservice.dto.ValidationErrorResponse;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
  @ExceptionHandler(InvalidRequestException.class)
  public ResponseEntity<ErrorResponse> handleInvalidRequest(InvalidRequestException ex) {
    ErrorResponse error = new ErrorResponse(ex.getMessage(), "400");
    return ResponseEntity.badRequest().contentType(MediaType.APPLICATION_JSON).body(error);
  }

  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleNotFound(ResourceNotFoundException ex) {
    ErrorResponse error = new ErrorResponse(ex.getMessage(), "404");
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ValidationErrorResponse> handleValidation(
      MethodArgumentNotValidException ex) {
    Map<String, String> details =
        ex.getBindingResult().getFieldErrors().stream()
            .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
    ValidationErrorResponse error = new ValidationErrorResponse("Validation error", "400", details);
    return ResponseEntity.badRequest().body(error);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleGeneric(Exception ex) {
    ErrorResponse error = new ErrorResponse("An unexpected error occurred", "500");
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
  }

  @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
  public ResponseEntity<ErrorResponse> handleMediaTypeNotSupported(
      HttpMediaTypeNotSupportedException ex) {
    // Only handle POST /resources with invalid content type as per spec
    String invalidType = ex.getContentType() != null ? ex.getContentType().toString() : "unknown";
    String message =
        String.format("Invalid file format: %s. Only MP3 files are allowed", invalidType);
    ErrorResponse error = new ErrorResponse(message, "400");
    return ResponseEntity.badRequest().contentType(MediaType.APPLICATION_JSON).body(error);
  }
}
