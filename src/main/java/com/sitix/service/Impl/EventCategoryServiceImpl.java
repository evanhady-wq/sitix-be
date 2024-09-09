package com.sitix.service.Impl;

import com.sitix.exceptions.ResourceNotFoundException;
import com.sitix.model.dto.request.EventCategoryRequest;
import com.sitix.model.dto.response.EventCategoryResponse;
import com.sitix.model.entity.EventCategory;
import com.sitix.service.EventCategoryService;
import com.sitix.repository.EventCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EventCategoryServiceImpl implements EventCategoryService {
    private final EventCategoryRepository eventCategoryRepository;
    private EventCategoryResponse convertToResponse(EventCategory eventCategory){
        return EventCategoryResponse.builder()
                .id(eventCategory.getId())
                .categoryName(eventCategory.getCategoryName())
                .build();
    }
    public EventCategoryResponse createEventCategory(EventCategoryRequest eventCategoryRequest){
        EventCategory eventCategory=EventCategory.builder()
                .categoryName(eventCategoryRequest.getCategoryName())
                .build();
        eventCategoryRepository.saveAndFlush(eventCategory);
        return convertToResponse(eventCategory);
    }

    public List<EventCategoryResponse> viewAllEventCategory(){
        return eventCategoryRepository.findAll().stream().map(this::convertToResponse).toList();
    }

    public EventCategoryResponse viewEventCategoryById(String id){
        return convertToResponse(eventCategoryRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Event Category Not Found")));
    }

    public EventCategoryResponse editEventCategory(EventCategoryRequest eventCategoryRequest){
        Optional<EventCategory> eventCategoryFound=eventCategoryRepository.findById(eventCategoryRequest.getId());
        EventCategory eventCategory=eventCategoryFound.orElseThrow(() -> new ResourceNotFoundException("Event Category Not Found"));
        eventCategory.setCategoryName(eventCategoryRequest.getCategoryName());
        eventCategoryRepository.saveAndFlush(eventCategory);
        return convertToResponse(eventCategory);
    }

    public void deleteEventCategory(String id){
        Optional<EventCategory> eventCategoryFound=eventCategoryRepository.findById(id);
        EventCategory eventCategory=eventCategoryFound.orElseThrow(() -> new ResourceNotFoundException("Event Category Not Found"));
        eventCategoryRepository.delete(eventCategory);
    }
}
