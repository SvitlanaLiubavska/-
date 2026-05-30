package com.example.eventapi.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.time.Instant;
import java.time.format.DateTimeFormatter;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DateMapperTest {
	@InjectMocks
	private DateMapper dateMapper;

	@Test
	void instantToIso() {
		Instant instant = Instant.now();

		String iso = dateMapper.instantToIso(instant);

		String expected = DateTimeFormatter.ISO_INSTANT.format(instant);

		assertEquals(iso, expected);
	}

	@Test
	void instantToIso_InstantNull() {
		String iso = dateMapper.instantToIso(null);
		assertNull(iso);
	}
}
