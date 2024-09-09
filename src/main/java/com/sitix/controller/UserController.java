package com.sitix.controller;

import com.sitix.constant.APIUrl;
import com.sitix.model.dto.response.AdminResponse;
import com.sitix.model.dto.response.CommonResponse;
import com.sitix.model.entity.User;
import com.sitix.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping(APIUrl.USER_API)
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_CREATOR','ROLE_CUSTOMER')")
    @GetMapping
    public ResponseEntity<CommonResponse<AdminResponse>> userPage(){
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
