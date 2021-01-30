package com.practice.jwtapp.service;

import com.practice.jwtapp.exception.PasswordResetTokenNotFoundException;
import com.practice.jwtapp.exception.PasswordResetTokenNotValidException;
import com.practice.jwtapp.model.PasswordResetToken;
import com.practice.jwtapp.model.User;
import com.practice.jwtapp.repository.PasswordResetTokenRepository;
import com.practice.jwtapp.testUtil.TestUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PasswordResetTokenServiceTest {
    @InjectMocks
    private PasswordResetTokenServiceImpl passwordResetTokenService;
    @Mock
    private PasswordResetTokenRepository passwordResetTokenRepository;
    private TestUtil testUtil;
    private User user;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        testUtil = new TestUtil();
        user = testUtil.createTestUser(1L, "user1@email.com", "password", "user");
    }

    @Test
    public void savePasswordResetToken() {
        Date date = Calendar.getInstance().getTime();
        String token = "91bb384e-24a8-47f3-8d71-b7c9f5b54270";
        PasswordResetToken passwordResetToken = testUtil.createPasswordResetToken(1L, date, token, user);

        when(passwordResetTokenRepository.save(passwordResetToken))
                .thenAnswer(i -> i.getArguments()[0]);

        PasswordResetToken savedPasswordResetToken = passwordResetTokenService.savePasswordResetToken(passwordResetToken);

        assertEquals("user1@email.com", savedPasswordResetToken.getUser().getEmail());
        verify(passwordResetTokenRepository, times(1)).save(passwordResetToken);
    }

    @Test
    public void validatePasswordResetToken() {
        PasswordResetToken passwordResetToken = passwordResetTokenService.createPasswordResetToken(user);
        final String token = passwordResetToken.getToken();

        when(passwordResetTokenRepository.findByToken(token))
                .thenAnswer(i -> Optional.of(passwordResetToken));

        passwordResetTokenService.validatePasswordResetToken(token);

        verify(passwordResetTokenRepository, times(1)).findByToken(token);
    }

    @Test
    public void validatePasswordResetTokenThrowsPasswordResetTokenNotFoundException() {
        PasswordResetToken passwordResetToken = passwordResetTokenService.createPasswordResetToken(user);
        final String token = passwordResetToken.getToken();

        when(passwordResetTokenRepository.findByToken(token))
                .thenAnswer(i -> Optional.empty());

        assertThrows(PasswordResetTokenNotFoundException.class, () ->
                passwordResetTokenService.validatePasswordResetToken(token));
    }

    @Test
    public void validatePasswordResetTokenThrowsPasswordResetTokenNotValidException() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = sdf.parse("2015-05-26");
        String token = "91bb384e-24a8-47f3-8d71-b7c9f5b54270";

        PasswordResetToken passwordResetToken = testUtil.createPasswordResetToken(1L, date, token, user);

        when(passwordResetTokenRepository.findByToken(token))
                .thenAnswer(i -> Optional.of(passwordResetToken));

        assertThrows(PasswordResetTokenNotValidException.class, () ->
                passwordResetTokenService.validatePasswordResetToken(token));
    }

    @Test
    public void createPasswordResetToken() {
        PasswordResetToken passwordResetToken = passwordResetTokenService.createPasswordResetToken(user);

        assertEquals("user1@email.com", passwordResetToken.getUser().getEmail());
        assertEquals("password", passwordResetToken.getUser().getPassword());
        assertFalse(passwordResetToken.getToken().isEmpty());
        assertFalse(passwordResetToken.getExpiryDate().toString().isEmpty());
    }
}
