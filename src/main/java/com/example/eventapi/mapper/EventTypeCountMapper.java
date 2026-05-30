package com.example.eventapi.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.example.eventapi.dto.EventTypeCount;
import com.example.eventapi.entity.EventTypeCountProjection;

@Mapper(componentModel = "spring")
public interface EventTypeCountMapper {
	EventTypeCount toEventTypeCount(EventTypeCountProjection projection);

	List<EventTypeCount> toEventTypeCounts(List<EventTypeCountProjection> projections);
}
