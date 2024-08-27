package com.sitix.model.service;

import com.sitix.model.dto.request.EventCategoryRequest;
import com.sitix.model.dto.response.EventCategoryResponse;

import java.util.List;

public interface EventCategoryService {
    EventCategoryResponse createEventCategory(EventCategoryRequest eventCategoryRequest);
    List<EventCategoryResponse> viewAllEventCategory();
    EventCategoryResponse editEventCategory(EventCategoryRequest eventCategoryRequest);
    public void deleteEventCategory(String id);
}
