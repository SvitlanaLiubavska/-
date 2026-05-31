package com.example.eventapi.query;

import com.example.eventapi.CreationUtil;
import com.example.eventapi.dto.EventFilter;
import com.example.eventapi.entity.EventEntity;
import com.example.eventapi.repository.EventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Pageable;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@Import(EventSpecificationBuilder.class)
public class EventSpecificationBuilderIT {
    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private EventSpecificationBuilder builder;

    @BeforeEach
    void setUp() {
        eventRepository.deleteAll();
        eventRepository.saveAll(List.of(
                CreationUtil.createEvent("user.signup", Instant.parse("2026-05-30T10:00:00Z"), "{\n  \"type\": \"user.signup\",\n  \"userId\": \"u-12345\",\n  \"email\": \"alice@example.com\",\n  \"source\": \"web\"\n}"),
                CreationUtil.createEvent("user.signup", Instant.parse("2026-05-29T10:00:00Z"), "{\n  \"type\": \"user.signup\",\n  \"userId\": \"u-12345\",\n  \"email\": \"alice@example.com\",\n  \"source\": \"web\"\n}"),
                CreationUtil.createEvent("order.created", Instant.parse("2026-05-30T12:00:00Z"), "{\n  \"type\": \"order.created\",\n  \"order\": {\n    \"id\": \"ord-998\",\n    \"total\": 42.50,\n    \"items\": [\n      { \"sku\": \"abc\", \"qty\": 2 },\n      { \"sku\": \"xyz\", \"qty\": 1 }\n    ]\n  }\n}"),
                CreationUtil.createEvent("order.created", Instant.parse("2026-05-30T10:00:00Z"), "{\n  \"type\": \"order.created\",\n  \"order\": {\n    \"id\": \"ord-998\",\n    \"total\": 42.50,\n    \"items\": [\n      { \"sku\": \"abc\", \"qty\": 2 },\n      { \"sku\": \"xyz\", \"qty\": 1 }\n    ]\n  }\n}")
        ));
    }

    @Test
    void filterByType() {
        var filter = new EventFilter("user.signup", null, null, Pageable.unpaged());

        var result = eventRepository.findAll(builder.build(filter));

        assertThat(result).hasSize(2);
        assertThat(result).allMatch(e -> e.getType().equals("user.signup"));
    }

    @Test
    void filterByFrom() {
        var filter = new EventFilter(null, "2026-05-29T00:00:00Z", null, Pageable.unpaged());

        var result = eventRepository.findAll(builder.build(filter));

        assertThat(result).hasSize(4);
    }

    @Test
    void filterByTo() {
        var filter = new EventFilter(null, null, "2026-05-29T11:00:00Z", Pageable.unpaged());

        var result = eventRepository.findAll(builder.build(filter));

        assertThat(result).hasSize(1);
    }

    @Test
    void filterBetween() {
        var filter = new EventFilter(null, "2026-05-29T11:00:00Z", "2026-05-30T11:00:00Z", Pageable.unpaged());

        var result = eventRepository.findAll(builder.build(filter));

        assertThat(result).hasSize(2);
    }

    @Test
    void fullFilter() {
        var filter = new EventFilter("order.created", "2026-05-29T11:00:00Z", "2026-05-30T11:00:00Z", Pageable.unpaged());

        var result = eventRepository.findAll(builder.build(filter));

        assertThat(result).hasSize(1);
    }
}
