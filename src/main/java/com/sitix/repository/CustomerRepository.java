package com.sitix.repository;

import com.sitix.model.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer,String> {
    @Query(value = "SELECT * FROM m_customer WHERE user_id = :userId", nativeQuery = true)
    Customer findByUserId(@Param("userId")String userId);

}
