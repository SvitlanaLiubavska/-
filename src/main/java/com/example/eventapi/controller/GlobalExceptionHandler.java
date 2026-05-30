package com.example.eventapi.controller;

import java.time.format.DateTimeParseException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.fasterxml.jackson.core.JsonProcessingException;

@RestControllerAdvice
public class GlobalExceptionHandler {
	@ExceptionHandler({JsonProcessingException.class})
	public ResponseEntity<ErrorResponse> handle(JsonProcessingException e) {
		return ResponseEntity.ok(ErrorResponse.builder(e, HttpStatus.BAD_REQUEST, "Cannot parse json").build());
	}

	@ExceptionHandler({IllegalArgumentException.class})
	public ResponseEntity<ErrorResponse> handle(IllegalArgumentException e) {
		return ResponseEntity.ok(ErrorResponse.builder(e, HttpStatus.BAD_REQUEST, "Illegal argument: " + e.getMessage()).build());
	}

	@ExceptionHandler({DateTimeParseException.class})
	public ResponseEntity<ErrorResponse> handle(DateTimeParseException e) {
		return ResponseEntity.ok(ErrorResponse.builder(e, HttpStatus.BAD_REQUEST, "Cannot parse string to instant").build());
	}

	@ExceptionHandler({RuntimeException.class})
	public ResponseEntity<ErrorResponse> handle(RuntimeException e) {
		return ResponseEntity.ok(ErrorResponse.builder(e, HttpStatus.BAD_REQUEST, "CSomething went wrong").build());
	}
}
