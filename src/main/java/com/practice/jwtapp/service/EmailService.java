package com.practice.jwtapp.service;

import com.practice.jwtapp.model.User;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;

public interface EmailService {
    String createResetUrl(String passwordResetToken, String host, String url);
    SimpleMailMessage createEmail(String subject, String body, User user);
    void sendMail(SimpleMailMessage email) throws MailException;
}
