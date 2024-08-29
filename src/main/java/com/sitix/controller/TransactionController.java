package com.sitix.controller;

import com.sitix.constant.APIUrl;
import com.sitix.model.dto.request.TransactionRequest;
import com.sitix.model.dto.response.CommonResponse;
import com.sitix.model.dto.response.TransactionResponse;
import com.sitix.model.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping(APIUrl.TRANSACTION_API)
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionService transactionService;

    private CommonResponse<TransactionResponse> generateTransactionResponse(String message, Optional<TransactionResponse> transaction) {
        return CommonResponse.<TransactionResponse>builder()
                .message(message)
                .data(transaction)
                .build();
    }

    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    @PostMapping
    public ResponseEntity<CommonResponse<TransactionResponse>> createTransaction(@RequestBody TransactionRequest transactionRequest) {
        TransactionResponse response = transactionService.createTransaction(transactionRequest);
        CommonResponse<TransactionResponse> responses = generateTransactionResponse("Success Add Transaction", Optional.of(response));
        return ResponseEntity.ok(responses);
    }

    @PostMapping("/notification")
    public ResponseEntity<String> handleMidtransNotification(@RequestBody Map<String, Object> notification) {
        try {
            System.out.println("Received notification: " + notification);
            transactionService.setTransactionStatus(notification);
            return ResponseEntity.ok("Notification Handled");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error handling notification");        }
    }
}
