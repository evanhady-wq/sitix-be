package com.sitix.model.dto.response;

import com.sitix.model.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class RegisterResponse {
    private String username;
    private Role role;
}