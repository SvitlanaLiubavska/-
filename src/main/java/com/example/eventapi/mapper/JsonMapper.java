package com.example.eventapi.mapper;

import java.util.Map;

import org.mapstruct.Named;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JsonMapper {
	private final ObjectMapper objectMapper;

	@Named("stringToMap")
	public Map<String, Object> stringToMap(String payload) {
		if (payload == null || payload.isEmpty()) {
			return Map.of();
		}
		try {
			return objectMapper.readValue(payload, new TypeReference<>() {
			});
		}
		catch (JsonProcessingException e) {
			throw new IllegalArgumentException(e.getMessage());
		}
	}
}
