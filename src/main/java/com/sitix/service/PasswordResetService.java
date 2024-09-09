package com.sitix.service;

import com.sitix.model.dto.request.ChangePasswordRequest;
import org.springframework.stereotype.Repository;

public interface PasswordResetService {
    void forgotPassword(String email);
    void resetPassword(String token, String newPassword);
    boolean changePassword(ChangePasswordRequest request);
}
