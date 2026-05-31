package com.example.eventapi.controller;

import com.example.eventapi.util.FormattedLogger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.eventapi.dto.EventStatistic;
import com.example.eventapi.service.EventStatisticService;

import lombok.RequiredArgsConstructor;

import java.util.Date;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/stats/summary")
public class StatisticController {
    private final EventStatisticService eventStatisticService;

    @GetMapping
    public ResponseEntity<EventStatistic> getEventStatistic() {
        long startTime = new Date().getTime();
        EventStatistic eventStatistic = eventStatisticService.getEventStatistic();
        FormattedLogger.logPerf("StatisticController", "getEventStatistic",
                "eventStatisticService.getEventStatistic()", Map.of(), new Date().getTime() - startTime);
        return ResponseEntity.ok(eventStatistic);
    }
}
