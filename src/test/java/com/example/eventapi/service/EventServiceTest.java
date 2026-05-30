package com.example.eventapi.service;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.example.eventapi.dto.Event;
import com.example.eventapi.dto.EventFilter;
import com.example.eventapi.entity.EventEntity;
import com.example.eventapi.factory.EventFactory;
import com.example.eventapi.mapper.EventMapper;
import com.example.eventapi.query.EventSpecificationBuilder;
import com.example.eventapi.repository.EventRepository;
import com.example.eventapi.repository.EventStatisticHourlyRepository;
import com.example.eventapi.service.impl.EventServiceImpl;

@ExtendWith(MockitoExtension.class)
class EventServiceTest {
	@Mock
	private EventRepository eventRepository;
	@Mock
	private EventMapper eventMapper;
	@Mock
	private EventFactory eventFactory;
	@Mock
	private EventSpecificationBuilder eventSpecificationBuilder;
	@Mock
	private EventStatisticHourlyRepository eventStatisticHourlyRepository;
	@Mock
	private ApplicationEventPublisher applicationEventPublisher;
	private EventService eventService;

	@BeforeEach
	void setUp() {
		eventService = new EventServiceImpl(eventRepository, eventMapper, eventFactory, eventSpecificationBuilder,
				eventStatisticHourlyRepository, applicationEventPublisher);
	}

	@Test
	void findById() {
		UUID id = UUID.randomUUID();
		EventEntity eventEntity = mock(EventEntity.class);

		when(eventRepository.findById(id)).thenReturn(Optional.of(eventEntity));
		when(eventMapper.eventEntityToEntity(eventEntity)).thenReturn(mock(Event.class));

		Optional<Event> response = eventService.findById(id);

		assertTrue(response.isPresent());
		verify(eventRepository).findById(id);
		verify(eventMapper).eventEntityToEntity(any(EventEntity.class));
	}

	@Test
	void findById_NotFound() {
		UUID id = UUID.randomUUID();

		when(eventRepository.findById(id)).thenReturn(Optional.empty());

		Optional<Event> response = eventService.findById(id);

		assertTrue(response.isEmpty());
		verify(eventRepository).findById(id);
		verify(eventMapper, never()).eventEntityToEntity(any(EventEntity.class));
	}

	@Test
	void findAll() {
		EventFilter eventFilter = new EventFilter(null, null, null, Pageable.unpaged());
		Page<EventEntity> eventEntityPage = new PageImpl<>(List.of(mock(EventEntity.class)));
		Specification<EventEntity> specification = mock(Specification.class);

		when(eventSpecificationBuilder.build(eventFilter)).thenReturn(specification);
		when(eventRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(eventEntityPage);

		List<Event> events = eventService.findAll(eventFilter);

		assertFalse(events.isEmpty());

		verify(eventSpecificationBuilder).build(any(EventFilter.class));
		verify(eventRepository).findAll(eq(specification), any(Pageable.class));
		verify(eventMapper).eventEntityToEntity(any(EventEntity.class));
	}

}
