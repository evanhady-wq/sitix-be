package com.sitix.repository;

import com.sitix.model.entity.Event;
import com.sitix.model.entity.TransactionDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionDetailRepository extends JpaRepository<TransactionDetail,String> {
    @Query(value = "SELECT * FROM m_transaction_detail WHERE transaction_id = :id", nativeQuery = true)
    List<TransactionDetail> findByTransactionId(@Param("id")String id);
}
