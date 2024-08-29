package com.sitix.repository;

import com.sitix.model.entity.EventCategory;
import com.sitix.model.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EventCategoryRepository extends JpaRepository<EventCategory,String> {
    Optional<EventCategory> findByCategoryName(String name);
}
