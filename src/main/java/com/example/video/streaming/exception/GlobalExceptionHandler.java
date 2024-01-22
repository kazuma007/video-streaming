package com.example.video.streaming.exception;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

  private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

  @ExceptionHandler(value = {EntityNotFoundException.class})
  protected ResponseEntity<Object> handleEntityNotFoundException(EntityNotFoundException e) {
    logger.error("Entity not found exception: {}", e.getMessage());
    return createResponseEntity(e.getMessage(), HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(value = {FileStorageException.class})
  protected ResponseEntity<Object> handleFileStorageException(FileStorageException e) {
    logger.error("File storage exception: {}", e.getMessage());
    return createResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(value = {Exception.class})
  protected ResponseEntity<Object> handleException(Exception e) {
    logger.error("Exception: {}", e.getMessage());
    return createResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
  }

  private ResponseEntity<Object> createResponseEntity(String message, HttpStatus status) {
    ErrorResponse response = new ErrorResponse(message, LocalDateTime.now(), status.value());
    return new ResponseEntity<>(response, status);
  }

  @Data
  @AllArgsConstructor
  private static class ErrorResponse {
    private String message;
    private LocalDateTime time;
    private int status;
  }
}
