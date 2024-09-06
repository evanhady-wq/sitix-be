package com.sitix.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionDetailResponse {
    private String id;
    private Integer quantity;
    private String transactionId;
    private String ticketCategoryName;
    private Double ticketCategoryPrice;
    private String eventName;
}
