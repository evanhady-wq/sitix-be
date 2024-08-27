package com.sitix.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TicketCategoryResponse {
    private String id;
    private String name;
    private Integer quota;
    private Double price;
}
