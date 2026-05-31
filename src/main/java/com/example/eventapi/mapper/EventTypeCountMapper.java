package com.example.eventapi.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.example.eventapi.dto.EventTypeCount;
import com.example.eventapi.entity.EventTypeCountProjection;
import org.springframework.stereotype.Component;

@Component
public class EventTypeCountMapper {
    private EventTypeCount toEventTypeCount(EventTypeCountProjection projection) {
        if (projection == null) {
            return null;
        }
        return new EventTypeCount(projection.getEventType(), projection.getEventCount());
    }

    public List<EventTypeCount> toEventTypeCounts(List<EventTypeCountProjection> projections) {
        if (projections == null || projections.isEmpty()) {
            return List.of();
        }
        return projections.stream().map(this::toEventTypeCount).toList();
    }
}
