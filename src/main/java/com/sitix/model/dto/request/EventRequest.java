package com.sitix.model.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sitix.model.entity.Creator;
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
    private String city;
    private String address;
    private String linkMap;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm", timezone = "Asia/Jakarta")
    private Date date;
//    private Image imagePath;
    private List<TicketCategoryRequest> ticketCategories;
}
