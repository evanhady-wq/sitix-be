package com.sitix.model.service.Impl;

import com.sitix.constant.APIUrl;
import com.sitix.exceptions.ResourceNotFoundException;
import com.sitix.model.dto.request.EventRequest;
import com.sitix.model.dto.request.TicketCategoryRequest;
import com.sitix.model.dto.response.EventResponse;
import com.sitix.model.dto.response.ImageResponse;
import com.sitix.model.dto.response.TicketCategoryResponse;
import com.sitix.model.entity.*;
import com.sitix.model.service.EventService;
import com.sitix.model.service.FileStorageService;
import com.sitix.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final EventCategoryRepository eventCategoryRepository;
    private final CreatorRepository creatorRepository;
    private final TicketCategoryRepository ticketCategoryRepository;
    private final FileStorageService fileStorageService;
    private final ImageRepository imageRepository;

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

        eventRepository.save(event);

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

//        User loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        Creator creator = creatorRepository.findByUserId(loggedInUser.getId());
//
//        List<TicketCategoryRequest> ticketCategoryRequestList = eventRequest.getTicketCategories();
//        List<TicketCategory> ticketCategories = new ArrayList<>();
//        for(TicketCategoryRequest ticketCategoryRequest : ticketCategoryRequestList){
//            TicketCategory ticketCategory = ticketCategoryService.createTicketCategory(ticketCategoryRequest);
//            ticketCategories.add(ticketCategory);
//        }

//        Optional<EventCategory> eventCategoryOptional =  eventCategoryRepository.findByCategoryName(eventRequest.getEventCategory());
//        EventCategory eventCategory = eventCategoryOptional.orElseThrow(() -> new ResourceNotFoundException("Event Category Not Found"));
//
//        Event event = Event.builder()
//                .name(eventRequest.getName())
//                .category(eventCategory)
//                .date(eventRequest.getDate())
//                .description(eventRequest.getDescription())
//                .location(eventRequest.getLocation())
//                .eventCreator(creator)
//                .ticketCategories(ticketCategories)
//                .build();
//
//        ticketCategories.forEach(ticket -> ticket.setEvent(event));
//
//        eventRepository.saveAndFlush(event);
//        return generateEventResponse(event);
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

    public void deleteTicketCategory(String id){
        Optional<TicketCategory> ticketCategoryOptional = ticketCategoryRepository.findById(id);
        TicketCategory ticketCategory = ticketCategoryOptional.orElseThrow(()-> new ResourceNotFoundException("Ticket Category Not Found"));

        ticketCategoryRepository.delete(ticketCategory);
    }

    public EventResponse updateEvent(EventRequest eventRequest) {
        User loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Creator creator = creatorRepository.findByUserId(loggedInUser.getId());

        List<Event> eventList = eventRepository.findByCreatorId(creator.getId());
        Optional<EventCategory> eventCategoryOptional =  eventCategoryRepository.findByCategoryName(eventRequest.getEventCategory());
        EventCategory eventCategory = eventCategoryOptional.orElseThrow(() -> new ResourceNotFoundException("Event Category Not Found"));

        Event updatedEvent = null;
        for (Event event : eventList) {
            if (event.getId().equals(eventRequest.getId())) {
                List<TicketCategory> ticketCategoryList = event.getTicketCategories();

                event.setName(eventRequest.getName());
                event.setCategory(eventCategory);
                event.setDescription(eventRequest.getDescription());
                event.setLocation(eventRequest.getLocation());
                event.setDate(eventRequest.getDate());

                List<TicketCategoryRequest> ticketCategoryListRequest=eventRequest.getTicketCategories();

                for (int i = 0; i < ticketCategoryList.size(); i++) {
                    if (i < ticketCategoryListRequest.size()) {
                        ticketCategoryList.get(i).setName(ticketCategoryListRequest.get(i).getName());
                        ticketCategoryList.get(i).setQuota(ticketCategoryListRequest.get(i).getQuota());
                        ticketCategoryList.get(i).setPrice(ticketCategoryListRequest.get(i).getPrice());
                    } else {
                        ticketCategoryList.remove(i);
                        i--;
                    }
                }

                for (int i = ticketCategoryList.size(); i < ticketCategoryListRequest.size(); i++) {
                    TicketCategory newCategory = new TicketCategory();
                    newCategory.setName(ticketCategoryListRequest.get(i).getName());
                    newCategory.setQuota(ticketCategoryListRequest.get(i).getQuota());
                    newCategory.setPrice(ticketCategoryListRequest.get(i).getPrice());
                    ticketCategoryList.add(newCategory);
                }

                updatedEvent = event;
                break;
            }
        }

        if (updatedEvent == null) {
            throw new ResourceNotFoundException("Event not found for update");
        }

        eventRepository.saveAndFlush(updatedEvent);
        return generateEventResponse(updatedEvent);
    }

    public EventResponse isDone(String id) {
        User loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Creator creator = creatorRepository.findByUserId(loggedInUser.getId());

        List<Event> eventList = eventRepository.findByCreatorId(creator.getId());

        Event updatedEvent = null;
        for (Event event : eventList) {
            if (event.getId().equals(id)) {
                event.setIsDone(true);
                updatedEvent = event;
                break;
            }
        }

        if (updatedEvent == null) {
            throw new ResourceNotFoundException("Event not found for update");
        }
        eventRepository.saveAndFlush(updatedEvent);
        return generateEventResponse(updatedEvent);
    }

    public EventResponse findEventById(String id){
        return generateEventResponse(eventRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Customer Not Found")));

    }

    public List<EventResponse> findEventByName(String eventName) {
        List<EventResponse> eventResponseList= eventRepository.findAllByNameContaining(eventName).stream().map(this::generateEventResponse)
                .toList();
        if(eventResponseList.size()<=0){
            throw new ResourceNotFoundException("Event Not Found");
        }else{
            return eventResponseList;
        }
//        return eventRepository.findAllByNameContaining(eventName).stream().map(this::generateEventResponse)
//                .collect(Collectors.toList());
    }

    public List<EventResponse> findEventByCategory(String category) {
        List<EventResponse> eventResponseList= eventRepository.findByEventCategory(category).stream().map(this::generateEventResponse)
                .toList();
        if(eventResponseList.size()<=0){
            throw new ResourceNotFoundException("Event Not Found");
        }else{
            return eventResponseList;
        }
//        return eventRepository.findByEventCategory(category).stream().map(this::generateEventResponse)
//                .collect(Collectors.toList());
    }

    public List<EventResponse> viewUpcomingEvent() {
        return eventRepository.findUpcomingEvents().stream().map(this::generateEventResponse)
                .toList();
    }

    @Transactional
    public ImageResponse uploadPoster (MultipartFile file){
        User loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String fileName = fileStorageService.storeFile(file, loggedInUser.getId());

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(APIUrl.EVENT_API)
                .path("/poster/")
                .path(fileName)
                .toUriString();

        Image poster = Image.builder()
                .name(fileName)
                .contentType(file.getContentType())
                .path(fileDownloadUri)
                .build();

        imageRepository.saveAndFlush(poster);

        return ImageResponse.builder()
                .name(poster.getName())
                .size(file.getSize())
                .contentType(poster.getContentType())
                .path(poster.getPath())
                .build();
    }

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
