package com.sitix.controller;

import com.sitix.constant.APIUrl;
import com.sitix.model.dto.request.*;
import com.sitix.model.dto.response.CommonResponse;
import com.sitix.model.dto.response.LoginResponse;
import com.sitix.model.dto.response.RegisterResponse;
import com.sitix.service.AuthService;
import com.sitix.service.PasswordResetService;
import com.sitix.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping(APIUrl.AUTH_API)
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final UserService userService;
    private final PasswordResetService passwordResetService;

    @PostMapping("/register/customer")
    public ResponseEntity<CommonResponse<RegisterResponse>> registerCustomer(@RequestBody RegisterRequest<CustomerRequest> registerRequest) {
        RegisterResponse registered = authService.registerCustomer(registerRequest);
        CommonResponse<RegisterResponse> response = generateRegisterResponse(HttpStatus.CREATED.value(),Optional.of(registered));
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PostMapping("/register/creator")
    public ResponseEntity<CommonResponse<RegisterResponse>> registerCreator(@RequestBody RegisterRequest<CreatorRequest> registerRequest) {
        RegisterResponse registered = authService.registerCreator(registerRequest);
        CommonResponse<RegisterResponse> response = generateRegisterResponse(HttpStatus.CREATED.value(),Optional.of(registered));
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PostMapping("/register/admin")
    public ResponseEntity<CommonResponse<RegisterResponse>> registerAdmin(@RequestBody RegisterRequest<CreatorRequest> registerRequest) {
        RegisterResponse registered = authService.registerAdmin(registerRequest);
        CommonResponse<RegisterResponse> response = generateRegisterResponse(HttpStatus.CREATED.value(),Optional.of(registered));
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }


    @PostMapping("/login")
    public ResponseEntity<CommonResponse<LoginResponse>> login(@RequestBody LoginRequest loginRequest) {
        LoginResponse login = authService.login(loginRequest);
        CommonResponse<LoginResponse> response = CommonResponse.<LoginResponse>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Login Success")
                .data(Optional.of(login))
                .build();

        return ResponseEntity.ok(response);

    }

    private CommonResponse<RegisterResponse> generateRegisterResponse(Integer code, Optional<RegisterResponse> registerResponse) {
        return CommonResponse.<RegisterResponse>builder()
                .statusCode(code)
                .message("Account Successfully Registered!")
                .data(registerResponse)
                .build();
    }

    @PostMapping("/forgot")
    public ResponseEntity<CommonResponse<?>> forgotPassword(@RequestParam String email) {
        passwordResetService.forgotPassword(email);
        CommonResponse<?> response = CommonResponse.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Password reset link sent to your email")
                .data(Optional.empty())
                .build();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/reset")
    public ResponseEntity<?> resetPassword(@RequestParam String token, @RequestBody String newPassword) {
        passwordResetService.resetPassword(token, newPassword);
        CommonResponse<?> response = CommonResponse.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Password has been reset successfully")
                .data(Optional.empty())
                .build();
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_CREATOR','ROLE_CUSTOMER')")
    @PutMapping("/change-password")
    public ResponseEntity<CommonResponse<?>> changePassword(@RequestBody ChangePasswordRequest request) {
        CommonResponse<?> response = CommonResponse.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Password changed successfully")
                .data(Optional.empty())
                .build();

        CommonResponse<?> failedResponse = CommonResponse.builder()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .message("Old password incorrect")
                .data(Optional.empty())
                .build();

        boolean isChanged = passwordResetService.changePassword(request);
        if (isChanged) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.ok(failedResponse);
        }
    }
}
