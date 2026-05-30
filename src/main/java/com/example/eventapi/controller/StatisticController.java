package com.example.eventapi.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.eventapi.dto.EventStatistic;
import com.example.eventapi.service.EventStatisticService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/stats/summary")
public class StatisticController {
	private final EventStatisticService eventStatisticService;

	//Here context is matter, if for example this statistic is for dashboard then it's better to make separate endpoints for each metric
	// in order not to block the whole dashboard, but to display it widget per widget
	//I am using 2 sources of data event_statistic_hourly for statistic group by type and hour and real data from event table for the last period
	@GetMapping
	public EventStatistic getEventStatistic() {
		return eventStatisticService.getEventStatistic();
	}
}
