package com.practice.jwtapp.service;

import com.practice.jwtapp.exception.AccountTokenNotFoundException;
import com.practice.jwtapp.exception.AccountTokenNotValidException;
import com.practice.jwtapp.exception.EntityNotFoundException;
import com.practice.jwtapp.model.ConfirmAccountToken;
import com.practice.jwtapp.model.User;
import com.practice.jwtapp.repository.ConfirmAccountTokenRepository;
import com.practice.jwtapp.testUtil.TestUtil;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.junit.jupiter.api.Assertions.*;

public class ConfirmAccountServiceTest {
    @InjectMocks
    private ConfirmAccountServiceImpl confirmAccountService;
    @Mock
    private ConfirmAccountTokenRepository confirmAccountTokenRepository;

    private TestUtil testUtil;

    private User testUser;

    @BeforeEach
    public void setup() {
        testUtil = new TestUtil();
        testUser = testUtil.createTestUser(1L, "user@email.com", "password", "user");
        initMocks(this);
    }

    @Test
    public void findConfirmAccountTokenByTokenTest() {
        ConfirmAccountToken confirmAccountToken = testUtil.createConfirmAccountToken(testUser);

        when(confirmAccountTokenRepository.findByToken(anyString()))
                .thenAnswer(function -> Optional.of(confirmAccountToken));

        ConfirmAccountToken foundConfirmAccountToken = confirmAccountService.findConfirmAccountTokenByToken("token");

        assertEquals("user@email.com", foundConfirmAccountToken.getUser().getEmail());

        verify(confirmAccountTokenRepository).findByToken(anyString());
    }

    @Test
    public void findConfirmAccountTokenByTokenThrowsEntityNotFoundExceptionTest() {
        assertThrows(AccountTokenNotFoundException.class, () ->
            confirmAccountService.findConfirmAccountTokenByToken("token")
        );

        verify(confirmAccountTokenRepository).findByToken(anyString());
    }

    @Test
    public void validateConfirmAccountToken() {
        ConfirmAccountToken confirmAccountToken = testUtil.createConfirmAccountToken(testUser);

        when(confirmAccountTokenRepository.findByToken(anyString()))
                .thenAnswer(function -> Optional.of(confirmAccountToken));

        confirmAccountService.validateConfirmAccountToken("token");
    }

    @Test
    public void validateConfirmAccountAccountTokenNotValidExceptionTest() {
        ConfirmAccountToken confirmAccountToken = testUtil.createExpiredConfirmAccountToken(testUser);

        when(confirmAccountTokenRepository.findByToken(anyString()))
                .thenAnswer(function -> Optional.of(confirmAccountToken));

        assertThrows(AccountTokenNotValidException.class, () -> {
            confirmAccountService.validateConfirmAccountToken("token");
        });
    }

    @Test
    public void validateConfirmAccountAccountTokenNotFoundExceptionTest() {
        assertThrows(AccountTokenNotFoundException.class, () -> {
            confirmAccountService.validateConfirmAccountToken("token");
        });
    }

    @Test
    public void createConfirmAccountTokenTest() {
        ConfirmAccountToken confirmAccountToken = confirmAccountService.createConfirmAccountToken(testUser);

        assertEquals("user@email.com", confirmAccountToken.getUser().getEmail());
        assertFalse(confirmAccountToken.getExpiryDate().toString().isEmpty());
        assertFalse(confirmAccountToken.getToken().isEmpty());
        assertFalse(confirmAccountToken.getUser().isEnabled());
    }

    @Test
    public void saveConfirmAccountToken() {
        ConfirmAccountToken confirmAccountToken = testUtil.createConfirmAccountToken(testUser);

        when(confirmAccountTokenRepository.save(confirmAccountToken))
                .thenAnswer(function -> function.getArgument(0));

        ConfirmAccountToken savedConfirmAccountToken = confirmAccountService.saveConfirmAccountToken(confirmAccountToken);

        assertEquals("user@email.com", savedConfirmAccountToken.getUser().getEmail());

        verify(confirmAccountTokenRepository).save(confirmAccountToken);
    }
}
