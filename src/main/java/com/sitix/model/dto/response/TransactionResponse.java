package com.sitix.model.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sitix.model.entity.Transaction;
import com.sitix.model.entity.TransactionDetail;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionResponse {
    private String id;
    private LocalDateTime transactionDate;
    private String customerId;
    private Transaction.Status status;
    private List<TransactionDetailResponse> transactionDetails;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone = "Asia/Jakarta")
    private LocalDateTime paidAt;
    private String paymentUrl;
}
