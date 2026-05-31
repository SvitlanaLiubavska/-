package com.example.eventapi.service.impl;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.eventapi.util.FormattedLogger;
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
        Instant now = Instant.now();
        long start = System.currentTimeMillis();

        long totalEventCount = eventStatisticHourlyRepository.getTotalEventCount();
        FormattedLogger.logPerf("EventStatisticServiceImpl", "getEventStatistic",
                "eventStatisticService.getEventStatistic()", Map.of(), System.currentTimeMillis() - start);

        start = System.currentTimeMillis();
        List<EventTypeCountProjection> eventTypeCountProjections = eventStatisticHourlyRepository.getTotalEventCountByType();
        FormattedLogger.logPerf("EventStatisticServiceImpl", "getEventStatistic",
                "eventStatisticHourlyRepository.getTotalEventCountByType()", Map.of(), System.currentTimeMillis() - start);
        List<EventTypeCount> totalEventCountByType =
                eventTypeCountMapper.toEventTypeCounts(eventTypeCountProjections);

        start = System.currentTimeMillis();
        Instant startOfLast24Hour = now.minus(24, ChronoUnit.HOURS);
        long totalEventCountForLast24Hours = getTotalEventCountForLast24Hours(startOfLast24Hour, now);
        FormattedLogger.logPerf("EventStatisticServiceImpl", "getEventStatistic",
                "getTotalEventCountForLast24Hours()", Map.of("startOfLast24Hour", startOfLast24Hour, "now", now),
                System.currentTimeMillis() - start);

        start = System.currentTimeMillis();
        Instant startOfLast7Days = now.minus(7, ChronoUnit.DAYS);
        List<String> top5EventsTypesForLast7Days = getTop5EventsTypesForLast7Days(startOfLast7Days, now);
        FormattedLogger.logPerf("EventStatisticServiceImpl", "getEventStatistic",
                "getTop5EventsTypesForLast7Days()", Map.of("startOfLast7Days", startOfLast7Days, "now", now),
                System.currentTimeMillis() - start);

        return new EventStatistic(
                totalEventCount,
                totalEventCountByType,
                totalEventCountForLast24Hours,
                top5EventsTypesForLast7Days
        );
    }

    private long getTotalEventCountForLast24Hours(Instant from, Instant to) {
        long start = System.currentTimeMillis();
        Instant left = ceilHour(from);
        Instant right = floorHour(to);

        if (!left.isBefore(right)) {
            long eventCountBetween = eventRepository.getEventCountBetween(from, to);
            FormattedLogger.logPerf("EventStatisticServiceImpl", "getTotalEventCountForLast24Hours",
                    "eventRepository.getEventCountBetween", Map.of("from", from, "to", to),
                    System.currentTimeMillis() - start);
            return eventCountBetween;
        }

        long total = 0;

        total += eventRepository.getEventCountForEdges(from, left, right, to);
        FormattedLogger.logPerf("EventStatisticServiceImpl", "getTotalEventCountForLast24Hours",
                "eventRepository.getEventCountForEdges",
                Map.of("from", from, "left", left, "right", right, "to", to),
                System.currentTimeMillis() - start);
        start = System.currentTimeMillis();
        total += eventStatisticHourlyRepository.getEventCountBetween(left, right);
        FormattedLogger.logPerf("EventStatisticServiceImpl", "getTotalEventCountForLast24Hours",
                "eventStatisticHourlyRepository.getEventCountBetween", Map.of("left", left, "right", right),
                System.currentTimeMillis() - start);

        return total;
    }

    private Map<String, Long> getCountByType(Instant from, Instant to) {
        long start = System.currentTimeMillis();
        Instant left = ceilHour(from);
        Instant right = floorHour(to);

        Map<String, Long> merged = new HashMap<>();

        if (!left.isBefore(right)) {
            List<EventTypeCountProjection> eventCountByTypeBetween = eventRepository.getEventCountByTypeBetween(from, to);
            FormattedLogger.logPerf("EventStatisticServiceImpl", "getCountByType",
                    "eventRepository.getEventCountByTypeBetween", Map.of("from", from, "to", to),
                    System.currentTimeMillis() - start);
            merge(merged, eventCountByTypeBetween);
            return merged;
        }

        List<EventTypeCountProjection> eventCountByTypeForEdges = eventRepository.getEventCountByTypeForEdges(from, left, right, to);
        FormattedLogger.logPerf("EventStatisticServiceImpl", "getCountByType",
                "eventRepository.getEventCountByTypeForEdges",
                Map.of("from", from, "left", left, "right", right, "to", to),
                System.currentTimeMillis() - start);
        merge(merged, eventCountByTypeForEdges);
        merge(merged, eventStatisticHourlyRepository.getEventCountByTypeBetween(left, right));
        FormattedLogger.logPerf("EventStatisticServiceImpl", "getCountByType",
                "eventStatisticHourlyRepository.getEventCountByTypeBetween", Map.of("left", left, "right", right),
                System.currentTimeMillis() - start);

        return merged;
    }

    private List<String> getTop5EventsTypesForLast7Days(Instant from, Instant to) {
        return getCountByType(from, to).entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(5)
                .map(Map.Entry::getKey)
                .toList();
    }

    private Instant ceilHour(Instant t) {
        Instant truncated = t.truncatedTo(ChronoUnit.HOURS);
        return t.equals(truncated) ? t : truncated.plus(1, ChronoUnit.HOURS);
    }

    private Instant floorHour(Instant t) {
        return t.truncatedTo(ChronoUnit.HOURS);
    }

    private void merge(Map<String, Long> merged, List<EventTypeCountProjection> projections) {
        projections.forEach(p ->
                merged.merge(p.getEventType(), p.getEventCount(), Long::sum)
        );
    }
}
