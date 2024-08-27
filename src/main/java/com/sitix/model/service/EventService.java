package com.sitix.model.service;

import com.sitix.model.dto.request.EventRequest;
import com.sitix.model.dto.response.EventResponse;

import java.util.List;

public interface EventService {
    EventResponse createEvent (EventRequest eventRequest);
    List<EventResponse> viewAllEvent ();
    List<EventResponse> viewCreatorEvent();
//    void deleteEvent(String id);
}
