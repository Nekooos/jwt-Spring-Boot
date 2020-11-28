package com.practice.jwtapp.controller;

import com.practice.jwtapp.model.User;
import com.practice.jwtapp.model.UserDataTransferObject;
import com.practice.jwtapp.service.UserService;
import com.practice.jwtapp.testUtil.TestUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.mockito.Mockito.times;

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
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void saveUser() {
        UserDataTransferObject userDto = testUtil.createUserDto();
        User user = testUtil.createTestUser();

        Mockito.when(userService.saveUser(userDto)).thenReturn(user);
        ResponseEntity<?> responseEntity = userController.saveUser(userDto);

        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Mockito.verify(userService, times(1)).saveUser(userDto);
    }
}
