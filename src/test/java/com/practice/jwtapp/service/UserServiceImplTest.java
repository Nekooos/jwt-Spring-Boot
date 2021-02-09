package com.practice.jwtapp.service;

import com.practice.jwtapp.model.*;
import com.practice.jwtapp.repository.ConfirmAccountTokenRepository;
import com.practice.jwtapp.repository.RoleRepository;
import com.practice.jwtapp.repository.UserRepository;
import com.practice.jwtapp.testUtil.TestUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.security.core.parameters.P;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class UserServiceImplTest {
    @InjectMocks
    private UserServiceImpl userService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    BCryptPasswordEncoder passwordEncoder;
    @Mock
    private EmailServiceImpl emailService;
    @Mock
    private ConfirmAccountService confirmAccountService;
    @Mock
    private PasswordResetTokenService passwordResetTokenService;

    private TestUtil testUtil;


    @BeforeEach
    public void setup() {
        testUtil = new TestUtil();
        initMocks(this);
    }

    @Test
    public void saveNewPassword() {
        PasswordDto passwordDto = testUtil.createPasswordDto();
        User user = testUtil.createTestUser(1L, "user1@mail.com", "oldPassword", "user");

        when(userRepository.findByEmail("user1@mail.com"))
                .thenAnswer(i -> Optional.of(user));
        when(userRepository.save(user))
                .thenAnswer(i -> i.getArguments()[0]);

        User savedUser = userService.saveNewPassword(passwordDto, "user1@mail.com");

        assertEquals(savedUser.getPassword(), "newPassword");

        verify(userRepository).findByEmail("user1@mail.com");
        verify(userRepository).save(user);
    }

    @Test
    public void confirmAccount() {
        User user = testUtil.createTestUser(1L, "user@mail.com", "password", "user");
        ConfirmAccountToken confirmAccountToken = testUtil.createConfirmAccountToken(user);

        when(confirmAccountService.createConfirmAccountToken(user))
                .thenReturn(confirmAccountToken);
        when(confirmAccountService.saveConfirmAccountToken(confirmAccountToken))
                .thenReturn(confirmAccountToken);
        doNothing().when(emailService).sendMail(any());

        User savedUser = userService.confirmAccount(user);
        assertEquals("user@mail.com", savedUser.getEmail());

        verify(emailService).sendMail(any());
        verify(confirmAccountService).createConfirmAccountToken(user);
        verify(confirmAccountService).saveConfirmAccountToken(confirmAccountToken);
    }

    @Test
    public void resetPassword() {
        User user = testUtil.createTestUser(1L, "user@mail.com", "password", "user");
        PasswordResetToken passwordResetToken = testUtil.createPasswordResetToken(user);

        when(userRepository.findByEmail("user@mail.com"))
                .thenAnswer(i -> Optional.of(user));
        when(passwordResetTokenService.createPasswordResetToken(user))
                .thenReturn(passwordResetToken);
        when(passwordResetTokenService.savePasswordResetToken(passwordResetToken))
                .thenReturn(passwordResetToken);
        doNothing().when(emailService).sendMail(any());

        User resetPasswordUser = userService.resetPassword("user@mail.com");
        assertEquals("user@mail.com", resetPasswordUser.getEmail());

        verify(emailService).sendMail(any());
        verify(userRepository).findByEmail("user@mail.com");
        verify(passwordResetTokenService).createPasswordResetToken(user);
        verify(passwordResetTokenService).savePasswordResetToken(passwordResetToken);
    }

    @Test
    public void enableAccount() {
        User notEnabledUser = testUtil.createTestUser(1L, "user@mail.com", "password", "user");
        ConfirmAccountToken confirmAccountToken = testUtil.createConfirmAccountToken(notEnabledUser);
        String token = confirmAccountToken.getToken();

        when(confirmAccountService.findConfirmAccountTokenByToken(token))
                .thenReturn(confirmAccountToken);
        when(userRepository.save(notEnabledUser))
                .thenAnswer(i -> i.getArguments()[0]);

        User enabledUser = userService.enableAccount(token);

        assertTrue(enabledUser.isEnabled());

        verify(confirmAccountService).findConfirmAccountTokenByToken(token);
        verify(userRepository).save(notEnabledUser);
    }

    @Test
    public void findByEmail() {
        when(userRepository.findByEmail("user@email.com"))
                .thenAnswer(i -> Optional.of(testUtil.createTestUser(1L, "user@email.com", "password", "user")));
        User user = userService.findByEmail("user@email.com");

        assertEquals(user.getEmail(), "user@email.com");

        verify(userRepository, times(1)).findByEmail("user@email.com");
    }

    @Test
    public void emailNotFound() {
        assertThrows(UsernameNotFoundException.class, () -> {
            userService.findByEmail("defaultUser");
        });
    }

    @Test
    public void saveUser() {
        UserDto userDto = testUtil.createUserDto("user@mail.com", "password");

        when(passwordEncoder.encode(Mockito.any(String.class)))
                .thenAnswer(i -> i.getArguments()[0]);
        when(roleRepository.findByName("USER")).thenReturn(new Role("USER"));
        when(userRepository.save(Mockito.any(User.class)))
                .thenAnswer(i -> i.getArguments()[0]);

        User resultUser =  userService.saveUser(userDto);

        assertEquals("user@mail.com", resultUser.getEmail());
    }
}
