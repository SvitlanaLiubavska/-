package com.example.eventapi.event;

import java.util.Map;
import java.util.UUID;

public record EventCreatedEvent (UUID id, Map<String, Object> body) {}
