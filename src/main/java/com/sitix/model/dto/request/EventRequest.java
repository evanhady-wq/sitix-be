package com.sitix.model.dto.request;

import com.sitix.model.entity.Creator;
import com.sitix.model.entity.EventCategory;
import com.sitix.model.entity.Image;
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
public class EventRequest {
    private String id;
    private String name;
    private String eventCategory;
    private Creator creator;
    private String description;
    private String location;
    private Date date;
//    private Image imagePath;
    private List<TicketCategoryRequest> ticketCategories;
}
