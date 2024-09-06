package com.sitix.controller;

import com.sitix.constant.APIUrl;
import com.sitix.model.dto.request.CreatorRequest;
import com.sitix.model.dto.response.CommonResponse;
import com.sitix.model.dto.response.CreatorResponse;
import com.sitix.model.dto.response.ImageResponse;
import com.sitix.model.service.CreatorService;
import com.sitix.model.service.FileStorageService;
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
@RequestMapping(APIUrl.CREATOR_API)
@RequiredArgsConstructor
public class CreatorController {
    private final CreatorService creatorService;
    private final FileStorageService fileStorageService;

    //CREATOR AUTHORITY
    @PreAuthorize("hasRole('ROLE_CREATOR')")
    @GetMapping("/profile")
    public ResponseEntity<CommonResponse<CreatorResponse>> viewMyProfile(){
        CreatorResponse creatorResponse = creatorService.viewCreatorProfile();
        CommonResponse<CreatorResponse> response = generateCreatorResponse(HttpStatus.OK.value(),"Profile", Optional.of(creatorResponse));
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ROLE_CREATOR')")
    @PutMapping("/profile")
    public ResponseEntity<CommonResponse<CreatorResponse>> editMyProfile(@Valid @RequestBody CreatorRequest creatorRequest) {
        CreatorResponse update = creatorService.editCreator(creatorRequest);
        CommonResponse<CreatorResponse> response = generateCreatorResponse(HttpStatus.OK.value(),"Update Success", Optional.of(update));

        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ROLE_CREATOR')")
    @PostMapping(path="/profilepicture", consumes = {MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<CommonResponse<ImageResponse>> uploadProfile(@RequestParam("profile") MultipartFile file) {
        ImageResponse response = creatorService.uploadProfile(file);
        CommonResponse<ImageResponse> commonResponse = CommonResponse.<ImageResponse>builder()
                .statusCode(HttpStatus.OK.value())
                .message("File uploaded successfully")
                .data(Optional.of(response))
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(commonResponse);
    }

    @GetMapping("/profilepicture/{fileName}")
    public ResponseEntity<ByteArrayResource> getProfile(@PathVariable String fileName) {
        byte[] image = fileStorageService.loadImage(fileName);
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG)
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + fileName + "\"")
                .body(new ByteArrayResource(image));
    }

    @PreAuthorize("hasRole('ROLE_CREATOR')")
    @DeleteMapping("/profile")
    public ResponseEntity<CommonResponse<CreatorResponse>> deleteAccount(){
        creatorService.deleteAccount();
        CommonResponse<CreatorResponse> response = generateCreatorResponse(HttpStatus.OK.value(),"Delete Success", Optional.empty());
        return ResponseEntity.ok(response);
    }

    //ADMIN AUTHORITY
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping
    public ResponseEntity<CommonResponse<List<CreatorResponse>>> getAllCreator(){
        List<CreatorResponse> creatorResponseList = creatorService.viewAllCreator();
        CommonResponse<List<CreatorResponse>> response = CommonResponse.<List<CreatorResponse>> builder()
                .statusCode(HttpStatus.OK.value())
                .message("Success Load All Creator")
                .data(Optional.of(creatorResponseList))
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping("view/{id}")
    public ResponseEntity<CommonResponse<CreatorResponse>> getCreatorById(@PathVariable String id) {
        CreatorResponse creatorResponse = creatorService.getById(id);
        CommonResponse<CreatorResponse> response = generateCreatorResponse(HttpStatus.OK.value(),"Search Result By ID", Optional.of(creatorResponse));
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<CommonResponse<CreatorResponse>> deleteAccountCreator(@PathVariable String id) {
        creatorService.deleteCreator(id);
        CommonResponse<CreatorResponse> response = generateCreatorResponse(HttpStatus.OK.value(),"Delete Success", Optional.empty());
        return ResponseEntity.ok(response);
    }

    private CommonResponse<CreatorResponse> generateCreatorResponse(Integer code, String message, Optional<CreatorResponse> creator) {
        return CommonResponse.<CreatorResponse>builder()
                .statusCode(code)
                .message(message)
                .data(creator)
                .build();
    }
}
