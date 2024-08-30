package com.sitix.repository;

import com.sitix.model.entity.Event;
import com.sitix.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,String> {
//    @Query(value = "SELECT * FROM m_user WHERE email = :email",nativeQuery = true)
    Optional<User> findByEmail(String email);

    Optional<User> findByUsername(String username);

}
