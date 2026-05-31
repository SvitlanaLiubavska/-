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
    @Query(value = """
                SELECT SUM(cnt) FROM (
                    SELECT COUNT(*) as cnt
                    FROM events
                    WHERE created_at >= :from AND created_at < :left
                    UNION ALL
                    SELECT COUNT(*) as cnt
                    FROM events
                    WHERE created_at >= :right AND created_at < :to
                ) t
            """, nativeQuery = true)
    long getEventCountForEdges(Instant from, Instant left, Instant right, Instant to);

    @Query("""
                SELECT COUNT(e)
                FROM EventEntity e
                WHERE e.createdAt >= :from
                  AND e.createdAt < :to
            """)
    long getEventCountBetween(Instant from, Instant to);

    @Query("""
                SELECT e.type as eventType, COUNT(e) as eventCount
                FROM EventEntity e
                WHERE e.createdAt >= :from
                  AND e.createdAt < :to
                GROUP BY e.type
            """)
    List<EventTypeCountProjection> getEventCountByTypeBetween(Instant from, Instant to);

    @Query(value = """
                SELECT type as eventType, SUM(cnt) as eventCount
                FROM (
                    SELECT e.type, COUNT(*) as cnt
                    FROM events e
                    WHERE e.created_at >= :from AND e.created_at < :left
                    GROUP BY e.type
                    UNION ALL
                    SELECT e.type, COUNT(*) as cnt
                    FROM events e
                    WHERE e.created_at >= :right AND e.created_at < :to
                    GROUP BY e.type
                ) t
                GROUP BY type
            """, nativeQuery = true)
    List<EventTypeCountProjection> getEventCountByTypeForEdges(Instant from, Instant left, Instant right, Instant to
    );
}
