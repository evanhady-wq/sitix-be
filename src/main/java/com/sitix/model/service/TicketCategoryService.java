package com.sitix.model.service;

import com.sitix.model.dto.request.TicketCategoryRequest;
import com.sitix.model.entity.TicketCategory;

public interface TicketCategoryService {
    TicketCategory createTicketCategory(TicketCategoryRequest ticketCategoryRequest);
}
