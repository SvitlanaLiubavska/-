package com.example.eventapi.factory;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.example.eventapi.entity.EventEntity;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class EventFactory {
	private final ObjectMapper objectMapper;

	public EventEntity create (Map<String, Object> body) {
		EventEntity eventEntity = new EventEntity();
		eventEntity.setId(UUID.randomUUID());
		eventEntity.setType((String) body.getOrDefault("type", ""));
		try {
			eventEntity.setPayload(objectMapper.writeValueAsString(body));
		}
		catch (JsonProcessingException e) {
			throw new IllegalArgumentException(e.getMessage());
		}
		eventEntity.setStatus("RECEIVED");
		eventEntity.setCreatedAt(Instant.now());
		return eventEntity;
	}
}
