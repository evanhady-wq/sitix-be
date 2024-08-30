package com.sitix.model.service.Impl;


import com.sitix.model.dto.request.TicketCategoryRequest;
import com.sitix.model.dto.response.TicketResponse;
import com.sitix.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class TicketServiceImpl {
    private final TicketRepository ticketRepository;

    public void generateTicket (){

        TicketResponse.builder()


                .build();
    }

}
