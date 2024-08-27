package com.sitix.model.service.Impl;

import ch.qos.logback.core.boolex.EventEvaluator;
import com.sitix.exceptions.ResourceNotFoundException;
import com.sitix.model.dto.request.EventRequest;
import com.sitix.model.dto.request.TicketCategoryRequest;
import com.sitix.model.dto.response.EventResponse;
import com.sitix.model.dto.response.TicketCategoryResponse;
import com.sitix.model.entity.*;
import com.sitix.model.service.EventService;
import com.sitix.repository.CreatorRepository;
import com.sitix.repository.EventCategoryRepository;
import com.sitix.repository.EventRepository;
import com.sitix.repository.TicketCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.StringTokenizer;

@Repository
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final EventCategoryRepository eventCategoryRepository;
    private final CreatorRepository creatorRepository;
    private final TicketCategoryRepository ticketCategoryRepository;


    //Create Event
    public EventResponse createEvent (EventRequest eventRequest){

        User loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Creator creator = creatorRepository.findByUserId(loggedInUser.getId());

        Optional<EventCategory> eventCategoryOptional =  eventCategoryRepository.findByCategoryName(eventRequest.getEventCategory());
        EventCategory eventCategory = eventCategoryOptional.orElseThrow(() -> new ResourceNotFoundException("Event Category Not Found"));

        Event event = Event.builder()
                .name(eventRequest.getName())
                .category(eventCategory)
                .date(eventRequest.getDate())
                .description(eventRequest.getDescription())
                .location(eventRequest.getLocation())
                .eventCreator(creator)
                .build();

        eventRepository.saveAndFlush(event);

        List<TicketCategoryRequest> ticketCategoryList = eventRequest.getTicketCategories();
        List<TicketCategory> ticketCategories = new ArrayList<>();
        for(TicketCategoryRequest ticketCategory : ticketCategoryList){
            TicketCategory ticketCategory1 = TicketCategory.builder()
                    .event(event)
                    .name(ticketCategory.getName())
                    .quota(ticketCategory.getQuota())
                    .price(ticketCategory.getPrice())
                    .build();

            ticketCategories.add(ticketCategory1);
            ticketCategoryRepository.saveAndFlush(ticketCategory1);
        }
        event.setTicketCategories(ticketCategories);
        eventRepository.saveAndFlush(event);
        return generateEventResponse(event);
    }

    public List<EventResponse> viewAllEvent (){
        return eventRepository.findAll().stream().map(this::generateEventResponse).toList();
    }

    public List<EventResponse> viewCreatorEvent(){
        User loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Creator creator = creatorRepository.findByUserId(loggedInUser.getId());

        return eventRepository.findByCreatorId(creator.getId()).stream().map(this::generateEventResponse).toList();
    }

//    public void deleteEvent(String id){
//      Optional<Event> eventOptional = eventRepository.findById(id);
//      Event event = eventOptional.orElseThrow(() -> new ResourceNotFoundException("Event Not Found"));
//
//      eventRepository.delete(event);
//    }


    private TicketCategoryResponse generateTicketCategoryResponse(TicketCategory ticketCategory){

        return TicketCategoryResponse.builder()
                .id(ticketCategory.getId())
                .name(ticketCategory.getName())
                .quota(ticketCategory.getQuota())
                .price(ticketCategory.getPrice())
                .build();
    }

    private EventResponse generateEventResponse(Event event){

        return EventResponse.builder()
                .id(event.getId())
                .name(event.getName())
                .eventCategory(event.getCategory().getCategoryName())
                .date(event.getDate())
                .creatorName(event.getEventCreator().getName())
                .ticketCategories(event.getTicketCategories().stream().map(this::generateTicketCategoryResponse).toList())
                .description(event.getDescription())
                .location(event.getLocation())
                .build();
    }


}
