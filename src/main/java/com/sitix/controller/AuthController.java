package com.sitix.controller;

import com.sitix.constant.APIUrl;
import com.sitix.model.dto.request.LoginRequest;
import com.sitix.model.dto.request.RegisterRequest;
import com.sitix.model.dto.request.CreatorRequest;
import com.sitix.model.dto.request.CustomerRequest;
import com.sitix.model.dto.response.CommonResponse;
import com.sitix.model.dto.response.LoginResponse;
import com.sitix.model.dto.response.RegisterResponse;
import com.sitix.model.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping(APIUrl.AUTH_API)
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register/customer")
    public ResponseEntity<CommonResponse<RegisterResponse>> registerCustomer(@RequestBody RegisterRequest<CustomerRequest> registerRequest) {
        RegisterResponse registered = authService.registerCustomer(registerRequest);
        CommonResponse<RegisterResponse> response = generateRegisterResponse(Optional.of(registered));
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PostMapping("/register/creator")
    public ResponseEntity<CommonResponse<RegisterResponse>> registerCreator(@RequestBody RegisterRequest<CreatorRequest> registerRequest) {
        RegisterResponse registered = authService.registerCreator(registerRequest);
        CommonResponse<RegisterResponse> response = generateRegisterResponse(Optional.of(registered));
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PostMapping("/register/admin")
    public ResponseEntity<CommonResponse<RegisterResponse>> registerAdmin(@RequestBody RegisterRequest<CreatorRequest> registerRequest) {
        RegisterResponse registered = authService.registerAdmin(registerRequest);
        CommonResponse<RegisterResponse> response = generateRegisterResponse(Optional.of(registered));
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }


    @PostMapping("/login")
    public ResponseEntity<CommonResponse<LoginResponse>> login(@RequestBody LoginRequest loginRequest) {
        LoginResponse login = authService.login(loginRequest);

        CommonResponse<LoginResponse> response = CommonResponse.<LoginResponse>builder()
                .message("Login Success")
                .data(Optional.of(login))
                .build();

        return ResponseEntity.ok(response);

    }

    private CommonResponse<RegisterResponse> generateRegisterResponse(Optional<RegisterResponse> registerResponse) {
        return CommonResponse.<RegisterResponse>builder()
                .message("Account Successfully Registered!")
                .data(registerResponse)
                .build();
    }

}
