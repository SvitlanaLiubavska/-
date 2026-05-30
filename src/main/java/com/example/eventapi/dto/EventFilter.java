package com.example.eventapi.dto;

import org.springframework.data.domain.Pageable;

public record EventFilter (String type,  String from,  String to, Pageable pageable) {
}
