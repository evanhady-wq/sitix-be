package com.sitix.service;

import com.sitix.model.dto.request.TransactionRequest;
import com.sitix.model.dto.response.TicketResponse;
import com.sitix.model.dto.response.TransactionResponse;

import java.util.List;
import java.util.Map;

public interface TransactionService {
    TransactionResponse createTransaction(TransactionRequest transactionRequest);
    void setTransactionStatus(Map<String, Object> notification);
    List<TransactionResponse> viewMyTransaction();
    List<TicketResponse> viewTicket ();
    TicketResponse viewTicketById(String id);
    void setTicketStatus(String id);
}
