package com.example.eventapi.repository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.eventapi.entity.EventStatisticHourlyEntity;
import com.example.eventapi.entity.EventTypeCountProjection;

@Repository
public interface EventStatisticHourlyRepository extends JpaRepository<EventStatisticHourlyEntity, UUID> {
	@Modifying
	@Transactional
	@Query(value = """
			    INSERT INTO event_statistic_hourly(period_start, type, count)
			    VALUES (:periodStart, :type, 1)
			    ON CONFLICT (period_start, type)
			    DO UPDATE SET count = event_statistic_hourly.count + 1
			""", nativeQuery = true)
	void increment(Instant periodStart, String type);

	@Query("""
			  SELECT SUM(e.count) FROM EventStatisticHourlyEntity e
			""")
	long getTotalEventCount();

	@Query("""
					SELECT e.id.type as eventType, SUM(e.count) as eventCount FROM EventStatisticHourlyEntity e GROUP BY e.id.type
			""")
	List<EventTypeCountProjection> getTotalEventCountByType();

	@Query("""
        SELECT SUM(e.count) FROM EventStatisticHourlyEntity e WHERE e.id.periodStart >= :from  AND e.id.periodStart < :to
    """)
	long getEventCountBetween(Instant from, Instant to);

	@Query("""
        SELECT e.id.type as eventType, SUM(e.count) as eventCount FROM EventStatisticHourlyEntity e
        WHERE e.id.periodStart >= :from AND e.id.periodStart < :to GROUP BY e.id.type
    """)
	List<EventTypeCountProjection> getEventCountByTypeBetween(Instant from, Instant to);
}
