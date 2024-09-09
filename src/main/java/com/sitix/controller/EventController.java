package com.sitix.controller;

import com.sitix.constant.APIUrl;
import com.sitix.model.dto.request.EventRequest;
import com.sitix.model.dto.response.CommonResponse;
import com.sitix.model.dto.response.EventResponse;
import com.sitix.model.dto.response.ImageResponse;
import com.sitix.service.EventService;
import com.sitix.service.FileStorageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

@RestController
@RequestMapping(APIUrl.EVENT_API)
@RequiredArgsConstructor
public class EventController {
    private final EventService eventService;
    private final FileStorageService fileStorageService;

    private CommonResponse<EventResponse> generateEventResponse(Integer code,String message, Optional<EventResponse> event) {
        return CommonResponse.<EventResponse>builder()
                .statusCode(code)
                .message(message)
                .data(event)
                .build();
    }

    @PreAuthorize("hasRole('ROLE_CREATOR')")
    @PostMapping(path="/poster/{id}", consumes = {MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<CommonResponse<ImageResponse>> uploadPoster(@RequestParam("poster") MultipartFile poster, @PathVariable String id) {
        ImageResponse response = eventService.uploadPoster(poster,id);
        CommonResponse<ImageResponse> commonResponse = CommonResponse.<ImageResponse>builder()
                .statusCode(HttpStatus.OK.value())
                .message("File uploaded successfully")
                .data(Optional.of(response))
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(commonResponse);
    }

    @GetMapping("/poster/{fileName}")
    public ResponseEntity<ByteArrayResource> getPoster(@PathVariable String fileName) {
        byte[] image = fileStorageService.loadImage(fileName);
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG)
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + fileName + "\"")
                .body(new ByteArrayResource(image));
    }

    @PreAuthorize("hasRole('ROLE_CREATOR')")
    @PostMapping
    public ResponseEntity<CommonResponse<EventResponse>> createEvent(@Valid @RequestBody EventRequest eventRequest) {
        EventResponse eventResponse = eventService.createEvent(eventRequest);
        CommonResponse<EventResponse> response = generateEventResponse(HttpStatus.OK.value(),"Success Create event", Optional.of(eventResponse));
        return ResponseEntity.ok(response);
    }


    @GetMapping("/allevent")
    public ResponseEntity<CommonResponse<List<EventResponse>>> viewAllEvent(){
        List<EventResponse> eventResponseList = eventService.viewAllEvent();
        CommonResponse<List<EventResponse>> response = CommonResponse.<List<EventResponse>> builder()
                .statusCode(HttpStatus.OK.value())
                .message("Success Load All Event")
                .data(Optional.of(eventResponseList))
                .build();

        return ResponseEntity.ok(response);
    }


    @GetMapping("/allevent/{id}")
    public ResponseEntity<CommonResponse<EventResponse>> viewEvent(@PathVariable String id){
        EventResponse eventResponse = eventService.findEventById(id);
        CommonResponse<EventResponse> response = generateEventResponse(HttpStatus.OK.value(),"Event By Id", Optional.of(eventResponse));

        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ROLE_CREATOR')")
    @GetMapping("/myevent")
    public ResponseEntity<CommonResponse<List<EventResponse>>> viewCreatorEvent(){
        List<EventResponse> eventResponseList = eventService.viewCreatorEvent();
        CommonResponse<List<EventResponse>> response = CommonResponse.<List<EventResponse>> builder()
                .statusCode(HttpStatus.OK.value())
                .message("Success Load Your Event")
                .data(Optional.of(eventResponseList))
                .build();

        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ROLE_CREATOR')")
    @PutMapping("/myevent")
    public ResponseEntity<CommonResponse<EventResponse>> updateMyEvent(@RequestBody EventRequest eventRequest){
        EventResponse eventResponseList = eventService.updateEvent(eventRequest);
        CommonResponse<EventResponse> response = generateEventResponse(HttpStatus.OK.value(),"Your Event Updated Successfully", Optional.of(eventResponseList));

        return ResponseEntity.ok(response);
    }

    @GetMapping("/allevent/search")
    public ResponseEntity<CommonResponse<List<EventResponse>>> viewEventByName(@RequestParam String name){
        List<EventResponse> eventResponseList = eventService.findEventByName(name);
        CommonResponse<List<EventResponse>> response = CommonResponse.<List<EventResponse>> builder()
                .statusCode(HttpStatus.OK.value())
                .message("Success Load Event")
                .data(Optional.of(eventResponseList))
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping("allevent/category/{id}")
    public ResponseEntity<CommonResponse<List<EventResponse>>> viewEventByCategory(@PathVariable String id){
        List<EventResponse> eventResponseList = eventService.findEventByCategory(id);
        CommonResponse<List<EventResponse>> response = CommonResponse.<List<EventResponse>> builder()
                .statusCode(HttpStatus.OK.value())
                .message("Success Load Event")
                .data(Optional.of(eventResponseList))
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping("allevent/upcoming")
    public ResponseEntity<CommonResponse<List<EventResponse>>> viewUpcomingEvent (){
        List<EventResponse> eventResponseList = eventService.viewUpcomingEvent();
        CommonResponse<List<EventResponse>> response = CommonResponse.<List<EventResponse>> builder()
                .statusCode(HttpStatus.OK.value())
                .message("Success Load Upcoming Event")
                .data(Optional.of(eventResponseList))
                .build();

        return ResponseEntity.ok(response);
    }






}