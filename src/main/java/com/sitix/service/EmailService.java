package com.sitix.service;

public interface EmailService {
    void sendEmail(String toEmail, String subject, String body);
}
