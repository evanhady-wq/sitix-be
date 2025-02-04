package com.sitix.service;

import com.sitix.model.dto.request.EventCategoryRequest;
import com.sitix.model.dto.response.EventCategoryResponse;

import java.util.List;

public interface EventCategoryService {
    EventCategoryResponse createEventCategory(EventCategoryRequest eventCategoryRequest);
    List<EventCategoryResponse> viewAllEventCategory();
    EventCategoryResponse viewEventCategoryById(String id);
    EventCategoryResponse editEventCategory(EventCategoryRequest eventCategoryRequest);
    void deleteEventCategory(String id);
}
