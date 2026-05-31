package com.example.eventapi.service.impl;

import java.util.*;

import com.example.eventapi.util.FormattedLogger;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.eventapi.dto.Event;
import com.example.eventapi.dto.EventFilter;
import com.example.eventapi.entity.EventEntity;
import com.example.eventapi.event.CreateKafkaEvent;
import com.example.eventapi.factory.EventFactory;
import com.example.eventapi.mapper.EventMapper;
import com.example.eventapi.query.EventSpecificationBuilder;
import com.example.eventapi.repository.EventRepository;
import com.example.eventapi.repository.EventStatisticHourlyRepository;
import com.example.eventapi.service.EventService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final EventRepository repository;
    private final EventMapper eventMapper;
    private final EventFactory eventFactory;
    private final EventSpecificationBuilder eventSpecificationBuilder;
    private final EventStatisticHourlyRepository eventStatisticHourlyRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Transactional
    @Override
    public UUID create(Map<String, Object> body) {
        EventEntity eventEntity = eventFactory.create(body);
        repository.save(eventEntity);
        long start = System.currentTimeMillis();
        eventStatisticHourlyRepository.increment(eventEntity.getCreatedAt(), eventEntity.getType());
        FormattedLogger.logPerf("EventServiceImpl", "create",
                "eventStatisticHourlyRepository.increment()",
                Map.of("createdAt", eventEntity.getCreatedAt(), "type", eventEntity.getType()),
                System.currentTimeMillis() - start);
        applicationEventPublisher.publishEvent(
                new CreateKafkaEvent(eventEntity.getId(), body)
        );
        return eventEntity.getId();
    }

    @Override
    public Optional<Event> findById(UUID id) {
        return repository.findById(id).map(eventMapper::eventEntityToEntity);
    }

    @Override
    public List<Event> findAll(EventFilter eventFilter) {
        Specification<EventEntity> specification = eventSpecificationBuilder.build(eventFilter);
        return repository.findAll(specification, eventFilter.pageable())
                .stream()
                .map(eventMapper::eventEntityToEntity)
                .toList();
    }

}
