package com.example.eventapi.entity;

import java.io.Serializable;
import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;

@Embeddable
@Getter
public class EventStatisticHourlyId implements Serializable {
	@Column(name = "period_start", nullable = false)
	private Instant periodStart;
	@Column(nullable = false, length = 100)
	private String type;
}
