package com.sitix.model.service.Impl;


import com.sitix.exceptions.ResourceNotFoundException;
import com.sitix.model.entity.PasswordResetToken;
import com.sitix.model.entity.User;
import com.sitix.model.service.CustomerService;
import com.sitix.model.service.EmailService;
import com.sitix.model.service.PasswordResetService;
import com.sitix.repository.PasswordResetTokenRepository;
import com.sitix.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PasswordResetServiceImpl implements PasswordResetService {
    private final UserRepository userRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;


    @Value("${app.resetPasswordUrl}")
    private String resetPasswordUrl;

    public void forgotPassword(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));

        PasswordResetToken token = passwordResetTokenRepository.findByUser(user);
        if (token != null) {
            passwordResetTokenRepository.delete(token);
        }

        String newToken = UUID.randomUUID().toString();
        PasswordResetToken passwordResetToken = new PasswordResetToken();
        passwordResetToken.setToken(newToken);
        passwordResetToken.setUser(user);
        passwordResetToken.setExpiryDate(LocalDateTime.now().plusHours(1));

        passwordResetTokenRepository.save(passwordResetToken);

        String resetUrl = resetPasswordUrl + "?token=" + newToken;
        emailService.sendEmail(user.getEmail(), "Reset Password", "Klik tautan berikut untuk mereset password Anda: " + resetUrl);
    }

    public void resetPassword(String token, String newPassword) {
        PasswordResetToken passwordResetToken = passwordResetTokenRepository.findByToken(token);

        if (passwordResetToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Token has expired");
        }

        User user = passwordResetToken.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        System.out.println("Pass: " + newPassword);
        System.out.println("Encode: " + user.getPassword());
        passwordResetTokenRepository.delete(passwordResetToken);
    }
}

