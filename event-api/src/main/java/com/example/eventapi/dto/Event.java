package com.example.eventapi.dto;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Event {
	private Map<String, Object> payload;
	private String status;
	private String createdAt;
}
