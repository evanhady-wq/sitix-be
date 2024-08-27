package com.sitix.controller;

import com.sitix.constant.APIUrl;
import com.sitix.model.dto.request.EventRequest;
import com.sitix.model.dto.response.CommonResponse;
import com.sitix.model.dto.response.CreatorResponse;
import com.sitix.model.dto.response.EventResponse;
import com.sitix.model.service.EventService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(APIUrl.EVENT_API)
@RequiredArgsConstructor
public class EventController {
    private final EventService eventService;

    private CommonResponse<EventResponse> generateEventResponse(String message, Optional<EventResponse> event) {
        return CommonResponse.<EventResponse>builder()
                .message(message)
                .data(event)
                .build();
    }

    @PreAuthorize("hasRole('ROLE_CREATOR')")
    @PostMapping
    public ResponseEntity<CommonResponse<EventResponse>> createEvent(@Valid @RequestBody EventRequest eventRequest) {
        EventResponse eventResponse = eventService.createEvent(eventRequest);
        CommonResponse<EventResponse> response = generateEventResponse("Success Create event", Optional.of(eventResponse));
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    @GetMapping("/all")
    public List<EventResponse> viewAllEvent(){
        return eventService.viewAllEvent();
    }

    @PreAuthorize("hasRole('ROLE_CREATOR')")
    @GetMapping("/myevent")
    public List<EventResponse> viewCreatorEvent(){
        return eventService.viewCreatorEvent();
    }


}