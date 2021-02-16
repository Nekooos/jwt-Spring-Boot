package com.practice.jwtapp.service;

import com.practice.jwtapp.model.User;
import com.practice.jwtapp.testUtil.TestUtil;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.Objects;
import java.util.UUID;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class EmailServiceTest {
    @InjectMocks
    EmailServiceImpl emailService;
    @Mock
    JavaMailSender javaMailSender;
    private TestUtil testUtil;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        testUtil = new TestUtil();
    }

    @Test
    public void sendEmail() {
        SimpleMailMessage email = testUtil.createEmail("subject", "from", "body", "to");
        emailService.sendMail(email);
        verify(javaMailSender, times(1)).send(email);
    }

    @Test
    public void createEmail() {
        User user = testUtil.createTestUser(1L, "user1@mail.com", "password", "user");
        SimpleMailMessage simpleMailMessage = emailService.createEmail("subject", "url", user);
        assertEquals("user1@mail.com", Objects.requireNonNull(simpleMailMessage.getTo())[0]);
    }

    @Test
    public void createResetUrl() {
        String token = "91bb384e-24a8-47f3-8d71-b7c9f5b54270";
        String path = "user/change-password";
        String url = emailService.createResetUrl(token, path);
        assertEquals("/user/changePassword?token=91bb384e-24a8-47f3-8d71-b7c9f5b54270", url);
    }
}
