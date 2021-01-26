package com.practice.jwtapp.controller;

import com.practice.jwtapp.model.User;
import com.practice.jwtapp.model.UserDto;
import com.practice.jwtapp.service.UserService;
import com.practice.jwtapp.testUtil.TestUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class UserControllerTest {
    @Mock
    private UserService userService;
    @InjectMocks
    private UserController userController;
    private TestUtil testUtil;

    @BeforeEach
    public void setup() {
        userController = new UserController();
        testUtil = new TestUtil();
        initMocks(this);
    }

    @Test
    public void saveUser() {
        UserDto userDto = testUtil.createUserDto();
        User user = testUtil.createTestUser(1L, "user", "password", "user");

        when(userService.saveUser(userDto)).thenReturn(user);
        ResponseEntity<?> responseEntity = userController.saveUser(userDto);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        verify(userService, times(1)).saveUser(userDto);
    }

    @Test
    public void getById() {
        when(userService.findById(1L))
                .thenReturn(testUtil.createTestUser(1L, "user", "password", "user"));

        ResponseEntity<?> responseEntity = userController.getUserById(1L);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        verify(userService, times(1)).findById(1L);
    }
}
