package com.sitix.model.service.Impl;

import com.sitix.exceptions.AuthenticationException;
import com.sitix.exceptions.ResourceNotFoundException;
import com.sitix.model.dto.request.LoginRequest;
import com.sitix.model.dto.request.RegisterRequest;
import com.sitix.model.dto.request.CreatorRequest;
import com.sitix.model.dto.request.CustomerRequest;
import com.sitix.model.dto.response.LoginResponse;
import com.sitix.model.dto.response.RegisterResponse;
import com.sitix.model.entity.Role;
import com.sitix.model.entity.User;
import com.sitix.model.service.AuthService;
import com.sitix.model.service.CreatorService;
import com.sitix.model.service.CustomerService;
import com.sitix.model.service.RoleService;
import com.sitix.repository.UserRepository;
import com.sitix.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final RoleService roleService;
    private final UserRepository userRepository;
    private final CustomerService customerService;
    private final CreatorService creatorService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @Override
    public RegisterResponse registerCustomer(RegisterRequest<CustomerRequest> registerRequest) {
        Role role = roleService.getOrSave(Role.UserRole.CUSTOMER);

        User user = User.builder()
                .email(registerRequest.getEmail().toLowerCase())
                .username(registerRequest.getUsername().toLowerCase())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .role(role)
                .build();

        user = userRepository.saveAndFlush(user);

        CustomerRequest customerRequest = registerRequest.getData().orElseThrow(
                () -> new ResourceNotFoundException("Customer Not Found")
        );

        customerRequest.setUser(user);

        customerService.createCustomer(customerRequest);

        return RegisterResponse.builder()
                .username(user.getUsername())
                .role(user.getRole())
                .build();
    }

    @Override
    public RegisterResponse registerCreator(RegisterRequest<CreatorRequest> registerRequest) {
        Role role = roleService.getOrSave(Role.UserRole.CREATOR);

        User user = User.builder()
                .email(registerRequest.getEmail().toLowerCase())
                .username(registerRequest.getUsername().toLowerCase())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .role(role)
                .build();

        user = userRepository.saveAndFlush(user);

        CreatorRequest requestData = registerRequest.getData().orElseThrow(
                () -> new ResourceNotFoundException("Creator Not Found")
        );

        requestData.setUser(user);
        creatorService.createCreator(requestData);

        return RegisterResponse.builder()
                .username(user.getUsername())
                .role(user.getRole())
                .build();
    }

    public RegisterResponse registerAdmin(RegisterRequest registerRequest) {
        Role role = roleService.getOrSave(Role.UserRole.ADMIN);

        User user = User.builder()
                .email(registerRequest.getEmail().toLowerCase())
                .username(registerRequest.getUsername().toLowerCase())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .role(role)
                .build();

        user = userRepository.saveAndFlush(user);

        return RegisterResponse.builder()
                .username(user.getUsername())
                .role(user.getRole())
                .build();
    }


    @Override
    public LoginResponse login(LoginRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername().toLowerCase(),
                            request.getPassword()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            User user = (User) authentication.getPrincipal();

            String token = jwtUtil.generateToken(user);

            return LoginResponse.builder()
                    .token(token)
                    .role(user.getRole())
                    .build();
        } catch (BadCredentialsException e) {
            throw new AuthenticationException("Username or Password Invalid");
        }
    }
}
