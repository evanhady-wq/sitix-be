package com.sitix.model.service.Impl;

import com.sitix.model.dto.request.TicketCategoryRequest;
import com.sitix.model.dto.response.TicketCategoryResponse;
import com.sitix.model.entity.TicketCategory;
import com.sitix.repository.TicketCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class TicketCategoryServiceImpl {
    private final TicketCategoryRepository ticketCategoryRepository;

//    public TicketCategory createTicketCategory(TicketCategoryRequest ticketCategoryRequest) {
//        TicketCategory ticketCategory = TicketCategory.builder()
//                .name(ticketCategoryRequest.getName())
//                .quota(ticketCategoryRequest.getQuota())
//                .price(ticketCategoryRequest.getPrice())
//                .build();
//
//        ticketCategoryRepository.save(ticketCategory);
//
//        return ticketCategory;
//    }


}
