package com.sitix.service;

import com.sitix.model.dto.request.ChangePasswordRequest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    UserDetails loadUserById(String id);
    UserDetails loadUserByEmail(String email);
}
