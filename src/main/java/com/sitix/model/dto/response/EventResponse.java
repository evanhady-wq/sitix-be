package com.sitix.model.dto.response;

import com.sitix.model.entity.Creator;
import com.sitix.model.entity.EventCategory;
import com.sitix.model.entity.TicketCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventResponse {
    private String id;
    private String name;
    private String eventCategory;
    private String description;
    private String location;
    private Date date;
    private List<TicketCategoryResponse> ticketCategories;
    private String creatorName;

}
