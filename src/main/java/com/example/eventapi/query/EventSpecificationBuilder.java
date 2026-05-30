package com.example.eventapi.query;

import java.time.Instant;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import com.example.eventapi.dto.EventFilter;
import com.example.eventapi.entity.EventEntity;

@Component
public class EventSpecificationBuilder {
	public Specification<EventEntity> build(EventFilter filter) {
		return Specification
				.where(typeEqual(filter.type()))
				.and(createdFrom(filter.from()))
				.and(createdTo(filter.to()));
	}

	private Specification<EventEntity> typeEqual(String type) {
		return ((root, query, criteriaBuilder) ->
				type == null ? null : criteriaBuilder.equal(root.get("type"), type));
	}

	private Specification<EventEntity> createdFrom(String from) {
		return ((root, query, criteriaBuilder) ->
				from == null ? null : criteriaBuilder.greaterThanOrEqualTo(root.get("createdAt"), Instant.parse(from)));
	}

	private Specification<EventEntity> createdTo(String to) {
		return ((root, query, criteriaBuilder) ->
				to == null ? null : criteriaBuilder.lessThanOrEqualTo(root.get("createdAt"), Instant.parse(to)));
	}
}
