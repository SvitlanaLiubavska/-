package com.example.eventapi.controller;

import java.time.format.DateTimeParseException;

import com.example.eventapi.dto.ApiError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.fasterxml.jackson.core.JsonProcessingException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler({JsonProcessingException.class})
    public ResponseEntity<ApiError> handle(JsonProcessingException e) {
        log.error("Error occurred", e);
        return buildResponse(e, HttpStatus.BAD_REQUEST, "Cannot parse json");
    }

    @ExceptionHandler({IllegalArgumentException.class})
    public ResponseEntity<ApiError> handle(IllegalArgumentException e) {
        log.error("Error occurred", e);
        return buildResponse(e, HttpStatus.BAD_REQUEST, "Illegal argument: " + e.getMessage());
    }

    @ExceptionHandler({DateTimeParseException.class})
    public ResponseEntity<ApiError> handle(DateTimeParseException e) {
        log.error("Error occurred", e);
        return buildResponse(e, HttpStatus.BAD_REQUEST, "Invalid date format");
    }

    @ExceptionHandler({RuntimeException.class})
    public ResponseEntity<ApiError> handle(RuntimeException e) {
        log.error("Error occurred", e);
        return buildResponse(e, HttpStatus.INTERNAL_SERVER_ERROR, "Something went wrong");
    }

    private ResponseEntity<ApiError> buildResponse(Exception e, HttpStatus status, String message) {
        return ResponseEntity
                .status(status.value())
                .body(
                        new ApiError(
                                HttpStatus.BAD_REQUEST,
                                e.getMessage()
                        )
                );
    }
}
