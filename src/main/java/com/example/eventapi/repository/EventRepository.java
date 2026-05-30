package com.example.eventapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import com.example.eventapi.entity.EventEntity;
import com.example.eventapi.entity.EventTypeCountProjection;

@Repository
public interface EventRepository extends JpaRepository<EventEntity, UUID>, JpaSpecificationExecutor<EventEntity> {

	@Query("""
        SELECT COUNT(e) FROM EventEntity e
        WHERE e.createdAt >= :from
    """)
	long getEventCountFrom(Instant from);

	@Query("""
        SELECT e.type as type, COUNT(e) as count
        FROM EventEntity e
        WHERE e.createdAt >= :from
        GROUP BY e.type
    """)
	List<EventTypeCountProjection> getEventCountByTypeFrom(Instant from);
}
