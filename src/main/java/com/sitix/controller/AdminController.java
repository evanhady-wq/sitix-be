package com.sitix.controller;

import com.sitix.constant.APIUrl;
import com.sitix.model.dto.response.AdminResponse;
import com.sitix.model.dto.response.CommonResponse;
import com.sitix.model.dto.response.CreatorResponse;
import com.sitix.model.entity.User;
import com.sitix.model.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping(APIUrl.ADMIN_API)
@RequiredArgsConstructor
public class AdminController {

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping
    public ResponseEntity<CommonResponse<AdminResponse>> adminPage(){
        User loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        AdminResponse admin = AdminResponse.builder()
                .username(loggedInUser.getUsername())
                .email(loggedInUser.getEmail())
                .build();

        CommonResponse<AdminResponse> response = CommonResponse.<AdminResponse>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Success Load Admin Data")
                .data(Optional.of(admin))
                .build();
        return ResponseEntity.ok(response);
    }
}
