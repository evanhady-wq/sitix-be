package com.sitix.controller;

import com.sitix.constant.APIUrl;
import com.sitix.model.dto.request.TransactionRequest;
import com.sitix.model.dto.response.CommonResponse;
import com.sitix.model.dto.response.TicketResponse;
import com.sitix.model.dto.response.TransactionResponse;
import com.sitix.model.service.TicketService;
import com.sitix.model.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping(APIUrl.TRANSACTION_API)
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionService transactionService;
//    private final TicketService ticketService;

    private CommonResponse<TransactionResponse> generateTransactionResponse(Integer code, String message, Optional<TransactionResponse> transaction) {
        return CommonResponse.<TransactionResponse>builder()
                .statusCode(code)
                .message(message)
                .data(transaction)
                .build();
    }
    private CommonResponse<List<TransactionResponse>> generateListTransactionResponse(Integer code, String message, Optional<List<TransactionResponse>> transaction) {
        return CommonResponse.<List<TransactionResponse>>builder()
                .statusCode(code)
                .message(message)
                .data(transaction)
                .build();
    }
    private CommonResponse<List<TicketResponse>> generateListTicketResponse(Integer code, String message, Optional<List<TicketResponse>> ticketResponses) {
        return CommonResponse.<List<TicketResponse>>builder()
                .statusCode(code)
                .message(message)
                .data(ticketResponses)
                .build();
    }

    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    @PostMapping
    public ResponseEntity<CommonResponse<TransactionResponse>> createTransaction(@RequestBody TransactionRequest transactionRequest) {
        TransactionResponse response = transactionService.createTransaction(transactionRequest);
        CommonResponse<TransactionResponse> responses = generateTransactionResponse(HttpStatus.OK.value(),"Success Add Transaction", Optional.of(response));
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

    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    @GetMapping
    public ResponseEntity<CommonResponse<List<TransactionResponse>>> viewMyTransaction(){
        List<TransactionResponse> response = transactionService.viewMyTransaction();
        CommonResponse<List<TransactionResponse>> response1 = generateListTransactionResponse(HttpStatus.OK.value(), "Success Load Transaction",Optional.of(response));
        return ResponseEntity.ok(response1);
    }
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    @GetMapping("/myticket")
    public ResponseEntity<CommonResponse<List<TicketResponse>>> viewMyTicket(){
        List<TicketResponse> response = transactionService.viewTicket();
        CommonResponse<List<TicketResponse>> response1 = generateListTicketResponse(HttpStatus.OK.value(), "Success Load Ticket",Optional.of(response));
        return ResponseEntity.ok(response1);
    }
}
