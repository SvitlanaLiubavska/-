package com.example.eventapi.mapper;

import com.example.eventapi.dto.Event;
import com.example.eventapi.entity.EventEntity;
import java.util.Map;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-05-29T21:45:27+0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17 (Azul Systems, Inc.)"
)
@Component
public class EventMapperImpl implements EventMapper {

    @Autowired
    private JsonMapper jsonMapper;
    @Autowired
    private DateMapper dateMapper;

    @Override
    public Event eventEntityToEntity(EventEntity eventEntity) {
        if ( eventEntity == null ) {
            return null;
        }

        String status = null;
        Map<String, Object> payload = null;
        String createdAt = null;

        status = eventEntity.getStatus();
        payload = jsonMapper.stringToMap( eventEntity.getPayload() );
        createdAt = dateMapper.instantToIso( eventEntity.getCreatedAt() );

        Event event = new Event( payload, status, createdAt );

        return event;
    }
}
