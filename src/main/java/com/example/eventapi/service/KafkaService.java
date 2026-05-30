package com.example.eventapi.service;

import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface KafkaService {
	void send(String id, Map<String, Object> body) throws JsonProcessingException;
}
