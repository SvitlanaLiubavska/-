package com.example.eventapi.service;

import com.example.eventapi.CreationUtil;
import com.example.eventapi.dto.EventStatistic;
import com.example.eventapi.repository.EventRepository;
import com.example.eventapi.repository.EventStatisticHourlyRepository;
import com.example.eventapi.service.impl.EventStatisticServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@Import(EventStatisticServiceImpl.class)
public class EventStatisticServiceIntegrationTest {
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private EventStatisticHourlyRepository statisticRepository;
    @Autowired
    private EventStatisticService statisticService;

    @BeforeEach
    void setUp() {
        eventRepository.deleteAll();
        statisticRepository.deleteAll();

        eventRepository.saveAll(List.of(
                CreationUtil.createEvent("user.signup", Instant.parse("2026-05-30T10:10:00Z"), "{\n  \"type\": \"user.signup\",\n  \"userId\": \"u-12345\",\n  \"email\": \"alice@example.com\",\n  \"source\": \"web\"\n}"),
                CreationUtil.createEvent("user.signup", Instant.parse("2026-05-30T10:20:00Z"), "{\n  \"type\": \"user.signup\",\n  \"userId\": \"u-12345\",\n  \"email\": \"alice@example.com\",\n  \"source\": \"web\"\n}"),
                CreationUtil.createEvent("user.signup", Instant.parse("2026-05-30T10:40:00Z"), "{\n  \"type\": \"user.signup\",\n  \"userId\": \"u-12345\",\n  \"email\": \"alice@example.com\",\n  \"source\": \"web\"\n}"),
                CreationUtil.createEvent("user.signup", Instant.parse("2026-05-30T11:10:00Z"), "{\n  \"type\": \"user.signup\",\n  \"userId\": \"u-12345\",\n  \"email\": \"alice@example.com\",\n  \"source\": \"web\"\n}"),
                CreationUtil.createEvent("user.signup", Instant.parse("2026-05-30T11:20:00Z"), "{\n  \"type\": \"user.signup\",\n  \"userId\": \"u-12345\",\n  \"email\": \"alice@example.com\",\n  \"source\": \"web\"\n}"),
                CreationUtil.createEvent("order.created", Instant.parse("2026-05-30T12:10:00Z"), "{\n  \"type\": \"order.created\",\n  \"order\": {\n    \"id\": \"ord-998\",\n    \"total\": 42.50,\n    \"items\": [\n      { \"sku\": \"abc\", \"qty\": 2 },\n      { \"sku\": \"xyz\", \"qty\": 1 }\n    ]\n  }\n}"),
                CreationUtil.createEvent("order.created", Instant.parse("2026-05-30T10:00:00Z"), "{\n  \"type\": \"order.created\",\n  \"order\": {\n    \"id\": \"ord-998\",\n    \"total\": 42.50,\n    \"items\": [\n      { \"sku\": \"abc\", \"qty\": 2 },\n      { \"sku\": \"xyz\", \"qty\": 1 }\n    ]\n  }\n}")
        ));

        statisticRepository.saveAll(List.of(
                CreationUtil.createStat("user.signup", Instant.parse("2026-05-30T11:00:00Z"), 3),
                CreationUtil.createStat("user.signup", Instant.parse("2026-05-30T12:00:00Z"), 2),
                CreationUtil.createStat("order.created", Instant.parse("2026-05-30T12:00:00Z"), 1),
                CreationUtil.createStat("order.created", Instant.parse("2026-05-30T10:00:00Z"), 1)
        ));
    }

    @Test
    void eventStatistic() {
        EventStatistic result = statisticService.getEventStatistic();
        assertThat(result.totalEventCount()).isEqualTo(7);
    }

    private Instant baseDay() {
        return Instant.now()
                .truncatedTo(java.time.temporal.ChronoUnit.DAYS)
                ;
    }

}
