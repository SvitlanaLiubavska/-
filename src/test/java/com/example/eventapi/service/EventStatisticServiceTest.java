package com.example.eventapi.service;


import com.example.eventapi.dto.EventStatistic;
import com.example.eventapi.dto.EventTypeCount;
import com.example.eventapi.entity.EventTypeCountProjection;
import com.example.eventapi.mapper.EventTypeCountMapper;
import com.example.eventapi.repository.EventRepository;
import com.example.eventapi.repository.EventStatisticHourlyRepository;
import com.example.eventapi.service.impl.EventStatisticServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EventStatisticServiceTest {

    @Mock
    private EventStatisticHourlyRepository eventStatisticHourlyRepository;

    @Mock
    private EventRepository eventRepository;

    @Mock
    private EventTypeCountMapper mapper;

    private EventStatisticServiceImpl service;
    
    @BeforeEach
    void setUp(){
        service = new EventStatisticServiceImpl(eventStatisticHourlyRepository, eventRepository, mapper);
    }

    @Test
    void getEventStatistic() {
        when(eventStatisticHourlyRepository.getTotalEventCount()).thenReturn(100L);

        EventTypeCountProjection projection = mock(EventTypeCountProjection.class);
        when(projection.getEventType()).thenReturn("CLICK");
        when(projection.getEventCount()).thenReturn(50L);

        when(eventStatisticHourlyRepository.getTotalEventCountByType()).thenReturn(List.of(projection));
        when(mapper.toEventTypeCounts(any())).thenReturn(List.of(new EventTypeCount("CLICK", 50L)));
        when(eventRepository.getEventCountForEdges(any(), any(), any(), any())).thenReturn(10L);
        when(eventStatisticHourlyRepository.getEventCountBetween(any(), any())).thenReturn(20L);
        when(eventRepository.getEventCountByTypeForEdges(any(), any(), any(), any())).thenReturn(List.of(projection));
        when(eventStatisticHourlyRepository.getEventCountByTypeBetween(any(), any())).thenReturn(List.of(projection));

        EventStatistic result = service.getEventStatistic();

        assertThat(result).isNotNull();
        assertThat(result.totalEventCount()).isEqualTo(100L);
        assertThat(result.totalEventCountByType()).hasSize(1);
        assertThat(result.eventCountForLast24Hours()).isGreaterThan(0);
        assertThat(result.top5EventsForLast7Days()).contains("CLICK");

        verify(eventStatisticHourlyRepository).getTotalEventCount();
        verify(eventStatisticHourlyRepository).getTotalEventCountByType();
    }

}
