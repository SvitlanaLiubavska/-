package com.example.eventapi.mapper;

import com.example.eventapi.dto.EventTypeCount;
import com.example.eventapi.entity.EventTypeCountProjection;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-05-29T22:16:55+0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17 (Azul Systems, Inc.)"
)
@Component
public class EventTypeCountMapperImpl implements EventTypeCountMapper {

    @Override
    public EventTypeCount toEventTypeCount(EventTypeCountProjection projection) {
        if ( projection == null ) {
            return null;
        }

        String eventType = null;
        long eventCount = 0L;

        EventTypeCount eventTypeCount = new EventTypeCount( eventType, eventCount );

        return eventTypeCount;
    }

    @Override
    public List<EventTypeCount> toEventTypeCounts(List<EventTypeCountProjection> projections) {
        if ( projections == null ) {
            return null;
        }

        List<EventTypeCount> list = new ArrayList<EventTypeCount>( projections.size() );
        for ( EventTypeCountProjection eventTypeCountProjection : projections ) {
            list.add( toEventTypeCount( eventTypeCountProjection ) );
        }

        return list;
    }
}
