package com.example.eventapi.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "event_statistic_hourly")
@Getter
@Setter
@NoArgsConstructor
public class EventStatisticHourlyEntity {
    @EmbeddedId
    private EventStatisticHourlyId id;
    @Column(nullable = false)
    private long count;
}
