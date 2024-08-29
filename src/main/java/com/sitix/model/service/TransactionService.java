package com.sitix.model.service;

import com.sitix.model.dto.request.TransactionRequest;
import com.sitix.model.dto.response.TransactionResponse;

import java.util.Map;

public interface TransactionService {
    TransactionResponse createTransaction(TransactionRequest transactionRequest);
    void setTransactionStatus(Map<String, Object> notification);
}
