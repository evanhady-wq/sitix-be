package com.sitix.model.dto.response;

import com.sitix.model.entity.TransactionDetail;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionResponse {
    private String id;
    private List<TransactionDetailResponse> transactionDetails;
    private String paymentUrl;
}
