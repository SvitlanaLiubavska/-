package com.example.eventapi.dto;

import java.util.List;

public record EventStatistic(
		long totalEventCount,
		List<EventTypeCount> totalEventCountByType,
		long eventCountForLast24Hours,
		List<String> top5EventsForLast7Days
) {}
