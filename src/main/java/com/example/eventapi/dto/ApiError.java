package com.example.eventapi.dto;

import com.example.eventapi.util.TraceIdUtil;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.Instant;
import java.util.UUID;

@Getter
public class ApiError {
    private final Instant timestamp;
    private final int status;
    private final String error;
    private final String message;
    private final String traceId;

    public ApiError(HttpStatus status, String message) {
        this.timestamp = Instant.now();
        this.status = status.value();
        this.error = status.getReasonPhrase();
        this.message = message;
        this.traceId = TraceIdUtil.getOrCreate();
    }
}
