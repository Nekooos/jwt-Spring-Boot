package com.practice.jwtapp.service;

import com.practice.jwtapp.repository.ConfirmAccountTokenRepository;
import com.practice.jwtapp.repository.PasswordResetTokenRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.time.Instant;
import java.util.Date;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class ScheduleTokenServiceImplTest {
    @InjectMocks
    private ScheduleTokenServiceImpl scheduleTokenService;
    @Mock
    ConfirmAccountTokenRepository confirmAccountTokenRepository;
    @Mock
    PasswordResetTokenRepository passwordResetTokenRepository;

    @BeforeEach
    public void setup() {
        initMocks(this);
    }

    @Test
    public void deleteExpiredTokensTokens() {
        doNothing().when(confirmAccountTokenRepository).deleteAllExpiredConfirmAccountToken(any(Date.class));
        doNothing().when(passwordResetTokenRepository).deleteAllExpiredPasswordResetTokens(any(Date.class));

        scheduleTokenService.deleteExpiredTokens();

        verify(confirmAccountTokenRepository).deleteAllExpiredConfirmAccountToken(any(Date.class));
        verify(passwordResetTokenRepository).deleteAllExpiredPasswordResetTokens(any(Date.class));
    }

}

