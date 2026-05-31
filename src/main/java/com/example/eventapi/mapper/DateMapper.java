package com.example.eventapi.mapper;

import java.time.Instant;
import java.time.format.DateTimeFormatter;

import org.mapstruct.Named;
import org.springframework.stereotype.Component;

@Component
public class DateMapper {
	@Named("instantToIso")
	public String instantToIso(Instant instant) {
		return instant == null ? null : DateTimeFormatter.ISO_INSTANT.format(instant);
	}
}
