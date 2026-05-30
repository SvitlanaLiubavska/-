package com.example.eventapi.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

class JsonMapperTest {
	private JsonMapper jsonMapper;

	@BeforeEach
	void setUp() {
		ObjectMapper objectMapper = new ObjectMapper();
		jsonMapper = new JsonMapper(objectMapper);
	}

	@Test
	void mapToEvent() throws JsonProcessingException {
		Map<String, Object> payload = new HashMap<>();
		payload.put("type", "user.signup");
		payload.put("userId", "u-12345");

		Map<String, Object> result = jsonMapper.stringToMap(new ObjectMapper().writeValueAsString(payload));

		assertEquals(payload, result);
	}

	@Test
	void mapToEvent_withException() {
		assertThrows(IllegalArgumentException.class, () -> jsonMapper.stringToMap("12121"));
	}
}
