package com.practice.jwtapp.service;

import com.practice.jwtapp.model.Role;
import com.practice.jwtapp.model.User;
import com.practice.jwtapp.model.UserDto;
import com.practice.jwtapp.repository.RoleRepository;
import com.practice.jwtapp.repository.UserRepository;
import com.practice.jwtapp.testUtil.TestUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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

    private TestUtil testUtil;


    @BeforeEach
    public void setup() {
        testUtil = new TestUtil();
        userService = new UserServiceImpl();
        initMocks(this);
    }

    @Test
    public void findByEmail() {
        when(userService.findById(1L))
                .thenReturn(testUtil.createTestUser(1L, "user", "password", "user"));
        User user = userService.findByEmail("defaultUser");
        assertEquals(user.getEmail(), "defaultUser");
        verify(userRepository, times(1)).findByEmail("defaultUser");
    }

    @Test
    public void emailNotFound() {
        assertThrows(UsernameNotFoundException.class, () -> {
            userService.findByEmail("defaultUser");
        });
    }

    @Test
    public void saveUser() {
        UserDto userDto = testUtil.createUserDto();

        when(passwordEncoder.encode(Mockito.any(String.class)))
                .thenAnswer(i -> i.getArguments()[0]);
        when(roleRepository.findByName("USER")).thenReturn(new Role("USER"));
        when(userRepository.save(Mockito.any(User.class)))
                .thenAnswer(i -> i.getArguments()[0]);

        User resultUser =  userService.saveUser(userDto);

        assertEquals("defaultUser", resultUser.getEmail());
    }
}
