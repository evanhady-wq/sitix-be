package com.sitix.model.dto.request;


import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionDetailRequest {
    private String id;
    private String tickerCategoryId;
    private Integer quantity;
}
