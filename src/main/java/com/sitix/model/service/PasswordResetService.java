package com.sitix.model.service;

import org.springframework.stereotype.Repository;

public interface PasswordResetService {
    void forgotPassword(String email);
    void resetPassword(String token, String newPassword);
}
