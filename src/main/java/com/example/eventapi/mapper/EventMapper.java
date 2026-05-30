package com.example.eventapi.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.example.eventapi.dto.Event;
import com.example.eventapi.entity.EventEntity;

@Mapper(componentModel = "spring", uses = {JsonMapper.class, DateMapper.class})
public interface EventMapper {
	@Mapping(source = "status", target = "status")
	@Mapping(source = "payload", target = "payload", qualifiedByName = "stringToMap")
	@Mapping(source = "createdAt", target = "createdAt", qualifiedByName = "instantToIso")
	Event eventEntityToEntity(EventEntity eventEntity);


}
