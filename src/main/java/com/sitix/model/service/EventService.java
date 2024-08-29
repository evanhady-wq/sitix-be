package com.sitix.model.service;

import com.sitix.model.dto.request.EventRequest;
import com.sitix.model.dto.response.EventResponse;
import com.sitix.model.dto.response.ImageResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface EventService {
    EventResponse createEvent (EventRequest eventRequest);
    List<EventResponse> viewAllEvent ();
    List<EventResponse> viewCreatorEvent();
    EventResponse findEventById(String id);
    EventResponse updateEvent(EventRequest eventRequest);
    List<EventResponse> findEventByName(String eventName);
    List<EventResponse> findEventByCategory(String category);
    List<EventResponse> viewUpcomingEvent();
    ImageResponse uploadPoster (MultipartFile file);

//    void deleteEvent(String id);
}
