package com.sitix.model.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
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
    private String eventCategoryId;
    private String eventCategory;
    private String description;
    private String city;
    private String address;
    private String linkMap;
    private String poster;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm", timezone = "Asia/Jakarta")
    private Date date;
    private List<TicketCategoryResponse> ticketCategories;
    private String creatorName;
    private String creatorId;

}
