package com.practice.jwtapp.service;

import com.practice.jwtapp.model.PasswordResetToken;
import com.practice.jwtapp.model.User;
import com.practice.jwtapp.repository.PasswordResetTokenRepository;
import com.practice.jwtapp.testUtil.TestUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Calendar;
import java.util.Date;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PasswordResetTokenServiceTest {
    @InjectMocks
    private PasswordResetTokenServiceImpl passwordResetTokenService;
    @Mock
    private PasswordResetTokenRepository passwordResetTokenRepository;
    private TestUtil testUtil;
    private User user;
    private final String token = "91bb384e-24a8-47f3-8d71-b7c9f5b54270";

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        testUtil = new TestUtil();
        user = testUtil.createTestUser(1L, "user1@email.com", "password", "user");
    }

    @Test
    public void savePasswordResetToken() {
        Date date = Calendar.getInstance().getTime();
        PasswordResetToken passwordResetToken = testUtil.createPasswordResetToken(1L, date, token, user);

        when(passwordResetTokenRepository.save(passwordResetToken))
                .thenAnswer(i -> i.getArguments()[0]);

        PasswordResetToken savedPasswordResetToken = passwordResetTokenService.savePasswordResetToken(passwordResetToken);

        assertEquals("user1@email.com", savedPasswordResetToken.getUser().getEmail());
        verify(passwordResetTokenRepository, times(1)).save(passwordResetToken);
    }

    @Test
    public void validatePasswordResetToken() {
        PasswordResetToken passwordResetToken = testUtil.createPasswordResetToken(1L, date, token, user);
    }
}
