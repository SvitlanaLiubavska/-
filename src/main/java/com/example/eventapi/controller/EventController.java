package com.example.eventapi.controller;

import com.example.eventapi.dto.Event;
import com.example.eventapi.dto.EventFilter;
import com.example.eventapi.service.EventService;
import com.fasterxml.jackson.core.JsonProcessingException;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/events")
public class EventController {
	private static final int MAX_PAGE_SIZE = 100;
	private static final int DEFAULT_PAGE_SIZE = 20;
	private final EventService eventService;

	@PostMapping
	public ResponseEntity<Map<String, Object>> create(@RequestBody Map<String, Object> body) throws JsonProcessingException {
		UUID id = eventService.create(body);

		Map<String, Object> response = new LinkedHashMap<>();
		response.put("id", id.toString());
		response.put("status", "accepted");
		return ResponseEntity.ok(response);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Event> findById(@PathVariable UUID id) {
		return eventService.findById(id)
				.map(eventEntity -> ResponseEntity.ok().body(eventEntity))
				.orElse(ResponseEntity.notFound().build());
	}

	//Max page size is 100 to balance usability and avoid to heavy db calls
	@GetMapping
	public ResponseEntity<List<Event>> findAll(@RequestParam String type, @RequestParam String from, @RequestParam String to,
			@PageableDefault(size = DEFAULT_PAGE_SIZE) Pageable pageable) {
		if(pageable.getPageSize() <= MAX_PAGE_SIZE) {
			throw new IllegalArgumentException("Max page size is " + MAX_PAGE_SIZE);
		}
		return ResponseEntity.ok(eventService.findAll(new EventFilter(type, from, to, pageable)));
	}


}
