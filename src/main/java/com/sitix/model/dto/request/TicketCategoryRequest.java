package com.sitix.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TicketCategoryRequest {
    private String id;
    private String eventId;
    private String name;
    private Integer quota;
    private Double price;

}
