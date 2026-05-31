package com.example.eventapi.entity;

import java.io.Serializable;
import java.time.Instant;

import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class EventStatisticHourlyId implements Serializable {

	private Instant periodStart;

	private String type;
}
