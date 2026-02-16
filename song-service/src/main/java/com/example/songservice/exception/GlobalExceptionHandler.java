package com.example.songservice.exception;

import com.example.songservice.dto.ErrorResponse;
import com.example.songservice.dto.ValidationErrorResponse;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
  @ExceptionHandler(InvalidRequestException.class)
  public ResponseEntity<ErrorResponse> handleInvalidRequest(InvalidRequestException ex) {
    ErrorResponse error = new ErrorResponse(ex.getMessage(), "400");
    return ResponseEntity.badRequest().header("Content-Type", "application/json").body(error);
  }

  @ExceptionHandler(SongNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleNotFound(SongNotFoundException ex) {
    ErrorResponse error = new ErrorResponse(ex.getMessage(), "404");
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
  }

  @ExceptionHandler(SongAlreadyExistsException.class)
  public ResponseEntity<ErrorResponse> handleConflict(SongAlreadyExistsException ex) {
    ErrorResponse error = new ErrorResponse(ex.getMessage(), "409");
    return ResponseEntity.status(HttpStatus.CONFLICT)
        .header("Content-Type", "application/json")
        .body(error);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ValidationErrorResponse> handleValidation(
      MethodArgumentNotValidException ex) {
    Map<String, String> details =
        ex.getBindingResult().getFieldErrors().stream()
            .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
    ValidationErrorResponse error = new ValidationErrorResponse("Validation error", "400", details);
    return ResponseEntity.badRequest().header("Content-Type", "application/json").body(error);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleGeneric(Exception ex) {
    ErrorResponse error = new ErrorResponse("An unexpected error occurred", "500");
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
  }
}
