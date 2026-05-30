package com.example.eventapi.service.impl;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.example.eventapi.service.KafkaService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class KafkaServiceImpl implements KafkaService {
	private static final String TOPIC = "events";
	private final KafkaTemplate<String, String> kafkaTemplate;
	private final ObjectMapper objectMapper;

	@Override
	public void send(String id, Map<String, Object> body) throws JsonProcessingException {
		Map<String, Object> message = new LinkedHashMap<>();
		message.put("id", id);
		message.put("payload", body);
		kafkaTemplate.send(TOPIC, id, objectMapper.writeValueAsString(message));
	}
}
