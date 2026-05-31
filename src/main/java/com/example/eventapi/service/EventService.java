package com.example.eventapi.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import com.example.eventapi.dto.Event;
import com.example.eventapi.dto.EventFilter;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface EventService {
	UUID create(Map<String, Object> body) throws JsonProcessingException;
	Optional<Event> findById(UUID id);
	List<Event> findAll(EventFilter eventFilter);
}
