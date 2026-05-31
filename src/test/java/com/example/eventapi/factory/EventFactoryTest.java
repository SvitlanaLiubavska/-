package com.example.eventapi.factory;

import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.example.eventapi.entity.EventEntity;
import com.fasterxml.jackson.databind.ObjectMapper;

class EventFactoryTest {
	private EventFactory eventFactory;

	@BeforeEach
	void setUp() {
		eventFactory = new EventFactory(new ObjectMapper());
	}

	@Test
	void createEvent() {
		Map<String, Object> body = Map.of("key", "value", "type", "type");
		EventEntity eventEntity = eventFactory.create(body);
		Assertions.assertNotNull(eventEntity);
		Assertions.assertEquals("type", eventEntity.getType());
	}
}
