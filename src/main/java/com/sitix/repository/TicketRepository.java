package com.sitix.repository;

import com.sitix.model.dto.response.TicketResponse;
import com.sitix.model.entity.Ticket;
import com.sitix.model.entity.TransactionDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket,String> {
    @Query(value = "SELECT * FROM m_ticket WHERE transaction_id = :id", nativeQuery = true)
    List<Ticket> findByTransactionId(@Param("id")String id);
}
