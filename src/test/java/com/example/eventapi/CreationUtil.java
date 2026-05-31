package com.example.eventapi;

import com.example.eventapi.entity.EventEntity;
import com.example.eventapi.entity.EventStatisticHourlyEntity;
import com.example.eventapi.entity.EventStatisticHourlyId;
import lombok.experimental.UtilityClass;

import java.time.Instant;
import java.util.UUID;

@UtilityClass
public class CreationUtil {
    public EventEntity createEvent(String type, Instant createdAt, String payload) {
        EventEntity eventEntity = new EventEntity();
        eventEntity.setId(UUID.randomUUID());
        eventEntity.setType(type);
        eventEntity.setCreatedAt(createdAt);
        eventEntity.setPayload(payload);
        eventEntity.setStatus("RECEIVED");
        return eventEntity;
    }

    public EventStatisticHourlyEntity createStat(String type, Instant periodStart, long count) {
        EventStatisticHourlyEntity eventStatisticHourlyEntity = new EventStatisticHourlyEntity();
        EventStatisticHourlyId eventStatisticHourlyId = new EventStatisticHourlyId();
        eventStatisticHourlyId.setType(type);
        eventStatisticHourlyId.setPeriodStart(periodStart);
        eventStatisticHourlyEntity.setId(eventStatisticHourlyId);
        eventStatisticHourlyEntity.setCount(count);
        return eventStatisticHourlyEntity;
    }
}
