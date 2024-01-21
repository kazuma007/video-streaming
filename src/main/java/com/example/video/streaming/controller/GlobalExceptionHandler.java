package com.example.video.streaming.controller;

import com.example.video.streaming.exception.EntityNotFoundException;
import com.example.video.streaming.exception.FileStorageException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

  private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

  @ExceptionHandler(value = {EntityNotFoundException.class})
  protected ResponseEntity<Object> handleEntityNotFoundException(
      EntityNotFoundException e, WebRequest request) {
    logger.error("Entity not found: {}", e.getMessage());
    return handleExceptionInternal(
        e, e.getMessage(), new HttpHeaders(), HttpStatus.NOT_FOUND, request);
  }

  @ExceptionHandler(value = {FileStorageException.class})
  protected ResponseEntity<Object> handleFileStorageException(
      FileStorageException e, WebRequest request) {
    logger.error("File storage error: {}", e.getMessage());
    return handleExceptionInternal(
        e, e.getMessage(), new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
  }
}
