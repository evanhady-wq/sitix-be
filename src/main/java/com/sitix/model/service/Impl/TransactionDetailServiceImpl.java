package com.sitix.model.service.Impl;


import com.sitix.exceptions.ResourceNotFoundException;
import com.sitix.model.dto.request.TransactionDetailRequest;
import com.sitix.model.dto.response.TransactionDetailResponse;
import com.sitix.model.entity.Event;
import com.sitix.model.entity.Ticket;
import com.sitix.model.entity.TicketCategory;
import com.sitix.model.entity.TransactionDetail;
import com.sitix.repository.EventRepository;
import com.sitix.repository.TicketCategoryRepository;
import com.sitix.repository.TransactionDetailRepository;
import lombok.*;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TransactionDetailServiceImpl {
    private final TransactionDetailRepository transactionDetailRepository;
    private final TicketCategoryRepository ticketCategoryRepository;
    private final EventRepository eventRepository;

}
