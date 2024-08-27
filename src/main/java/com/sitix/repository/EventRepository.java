package com.sitix.repository;

import com.sitix.model.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, String> {
    @Query(value = "SELECT * FROM m_event WHERE creator_id = :creatorId",nativeQuery = true)
    List<Event> findByCreatorId(@Param("creatorId") String creatorId);

    @Query(value = "SELECT * FROM m_event WHERE date > CURRENT_DATE", nativeQuery = true)
    List<Event> findUpcomingEvents();

    @Query(value = "SELECT * FROM m_event WHERE name ILIKE CONCAT('%', :name, '%')", nativeQuery = true)
    List<Event> findAllByNameContaining(@Param("name")String name);

    @Query(value = "SELECT * FROM m_event LEFT JOIN m_event.event_category_id ON m_event_category.id WHERE event_category_id.id = :category", nativeQuery = true)
    List<Event> findByEventCategory(@Param("category")String category);
}

