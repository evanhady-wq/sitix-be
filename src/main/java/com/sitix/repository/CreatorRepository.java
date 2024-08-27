package com.sitix.repository;

import com.sitix.model.dto.response.CreatorResponse;
import com.sitix.model.entity.Creator;
import com.sitix.model.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CreatorRepository extends JpaRepository<Creator,String> {
    @Query(value = "SELECT * FROM m_creator WHERE user_id =:userId", nativeQuery = true)
    Creator findByUserId(@Param("userId") String userId);

    @Query(value = "SELECT * FROM m_creator WHERE name ILIKE CONCAT('%', :name, '%')", nativeQuery = true)
    List<Creator> findCreatorByNameContaining(@Param("name")String name);
}
