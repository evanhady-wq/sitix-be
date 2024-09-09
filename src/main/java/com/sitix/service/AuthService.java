package com.sitix.service;

import com.sitix.model.dto.request.LoginRequest;
import com.sitix.model.dto.request.RegisterRequest;
import com.sitix.model.dto.request.CreatorRequest;
import com.sitix.model.dto.request.CustomerRequest;
import com.sitix.model.dto.response.LoginResponse;
import com.sitix.model.dto.response.RegisterResponse;


public interface AuthService {
    RegisterResponse registerCustomer(RegisterRequest<CustomerRequest> registerRequest);
    RegisterResponse registerCreator(RegisterRequest<CreatorRequest> registerRequest);
    RegisterResponse registerAdmin(RegisterRequest registerRequest);

    LoginResponse login(LoginRequest request);
}
