package com.sitix.controller;

import com.sitix.constant.APIUrl;
import com.sitix.model.dto.request.EventCategoryRequest;
import com.sitix.model.dto.response.CommonResponse;
import com.sitix.model.dto.response.EventCategoryResponse;
import com.sitix.model.service.EventCategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(APIUrl.EVENT_CATEGORY_API)
@RequiredArgsConstructor
public class EventCategoryController {
    private final EventCategoryService eventCategoryService;

    private CommonResponse<EventCategoryResponse> generateEventCategoryResponse(Integer code, String message, Optional<EventCategoryResponse> eventCategoryResponseOptional) {
        return CommonResponse.<EventCategoryResponse>builder()
                .statusCode(code)
                .message(message)
                .data(eventCategoryResponseOptional)
                .build();
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping
    public ResponseEntity<CommonResponse<EventCategoryResponse>> editEventCategory(@Valid @RequestBody EventCategoryRequest eventCategoryRequest) {
        EventCategoryResponse update = eventCategoryService.editEventCategory(eventCategoryRequest);
        CommonResponse<EventCategoryResponse> response = generateEventCategoryResponse(HttpStatus.OK.value(),"Update Event Category Success", Optional.of(update));
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<CommonResponse<List<EventCategoryResponse>>> getAllEventCategory(){
        List<EventCategoryResponse> eventCategoryResponseList = eventCategoryService.viewAllEventCategory();
        CommonResponse<List<EventCategoryResponse>> response = CommonResponse.<List<EventCategoryResponse>> builder()
                .statusCode(HttpStatus.OK.value())
                .message("Success Load All Category Event")
                .data(Optional.of(eventCategoryResponseList))
                .build();

        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<CommonResponse<EventCategoryResponse>> addEventCategory(@Valid @RequestBody EventCategoryRequest eventCategoryRequest) {
        EventCategoryResponse eventCategoryResponse = eventCategoryService.createEventCategory(eventCategoryRequest);
        CommonResponse<EventCategoryResponse> response = generateEventCategoryResponse(HttpStatus.OK.value(),"Success Create Category Event", Optional.of(eventCategoryResponse));
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<CommonResponse<?>> deleteEventCategory(@PathVariable String id) {
        eventCategoryService.deleteEventCategory(id);
        CommonResponse<?> response = CommonResponse.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Delete Success")
                .data(Optional.empty())
                .build();
        return ResponseEntity.ok(response);
    }

}
