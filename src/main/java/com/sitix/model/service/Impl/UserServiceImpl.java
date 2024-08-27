package com.sitix.model.service.Impl;

import com.sitix.model.service.UserService;
import com.sitix.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username).orElseThrow(()-> new UsernameNotFoundException("Invalid credential user"));
    }

    @Override
    public UserDetails loadUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(()-> new UsernameNotFoundException("Invalid credential user"));
    }

    @Override
    public UserDetails loadUserById(String id) {
        return userRepository.findById(id).orElseThrow(()-> new UsernameNotFoundException("Invalid credential user"));
    }
}
