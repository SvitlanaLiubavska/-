package com.example.eventapi.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "events")
@Getter
@NoArgsConstructor
@Setter
@AllArgsConstructor
public class EventEntity {
	@Id
	private UUID id;

	@Column(columnDefinition = "TEXT", nullable = false)
	private String payload;

	@Column(nullable = false, length = 100)
	private String type;

	@Column(nullable = false, length = 50)
	private String status;

	@Column(name = "created_at", nullable = false)
	private Instant createdAt;
}
