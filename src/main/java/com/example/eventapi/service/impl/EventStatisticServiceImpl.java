package com.example.eventapi.service.impl;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.example.eventapi.dto.EventStatistic;
import com.example.eventapi.dto.EventTypeCount;
import com.example.eventapi.entity.EventTypeCountProjection;
import com.example.eventapi.mapper.EventTypeCountMapper;
import com.example.eventapi.repository.EventRepository;
import com.example.eventapi.repository.EventStatisticHourlyRepository;
import com.example.eventapi.service.EventStatisticService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EventStatisticServiceImpl implements EventStatisticService {
	private final EventStatisticHourlyRepository eventStatisticHourlyRepository;
	private final EventRepository eventRepository;
	private final EventTypeCountMapper eventTypeCountMapper;

	@Override
	public EventStatistic getEventStatistic() {
		long totalEventCount = eventStatisticHourlyRepository.getTotalEventCount();
		List<EventTypeCount> totalEventCountByType =
				eventTypeCountMapper.toEventTypeCounts(eventStatisticHourlyRepository.getTotalEventCountByType());
		return new EventStatistic(
				totalEventCount,
				totalEventCountByType,
				getTotalEventCountForLast24Hours(),
				getTop5EventsTypesForLast7Days()
		);
	}

	private long getTotalEventCountForLast24Hours() {
		Instant now = Instant.now();
		Instant from = now.minus(24, ChronoUnit.HOURS);
		Instant currentHour = now.truncatedTo(ChronoUnit.HOURS);

		long statsPart = eventStatisticHourlyRepository.getEventCountBetween(from, currentHour);
		long livePart = eventRepository.getEventCountFrom(currentHour);

		return statsPart + livePart;
	}

	private List<String> getTop5EventsTypesForLast7Days() {
		Instant now = Instant.now();
		Instant from = now.minus(7, ChronoUnit.DAYS);
		Instant currentHour = now.truncatedTo(ChronoUnit.HOURS);

		Map<String, Long> merged = new HashMap<>();
		merge(merged, eventRepository.getEventCountByTypeFrom(currentHour));
		merge(merged, eventStatisticHourlyRepository.getEventCountByTypeBetween(from, currentHour));

		return merged.entrySet().stream()
				.sorted(Map.Entry.<String, Long>comparingByValue().reversed())
				.limit(5)
				.map(Map.Entry::getKey)
				.toList();
	}

	private void merge(Map<String, Long> merged , List<EventTypeCountProjection> projections){
		projections.forEach(p -> merged.put(p.getType(), p.getCount()));
	}

}
