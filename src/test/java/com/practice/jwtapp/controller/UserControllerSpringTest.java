package com.practice.jwtapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.practice.jwtapp.model.UserDto;
import com.practice.jwtapp.service.UserServiceImpl;
import com.practice.jwtapp.testUtil.TestUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerSpringTest {
    @MockBean
    private UserServiceImpl userService;
    @Autowired
    private MockMvc mockMvc;

    private TestUtil testUtil;
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        testUtil = new TestUtil();
        objectMapper = new ObjectMapper();
    }

    @Test
    @DisplayName("POST user/save throws MethodArgumentNotValidException")
    public void saveNotValidUser() throws Exception {
        UserDto user = testUtil.createUserDto("user@mail.com", "password");
        user.setEmail(null);

        when(userService.saveUser(user))
                .thenAnswer(i -> i.getArguments()[0]);

        mockMvc.perform(post("/user/save")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(user))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.message").value("Form validation failed"))
                .andExpect(jsonPath("$.fieldErrors").isArray())
                .andExpect(jsonPath("$.fieldErrors[?(@.field == \"email\")]").exists())
                .andExpect(jsonPath("$.fieldErrors[?(@.errorMessage == \"Email is required\")]").exists())
                .andExpect(status().is4xxClientError());
    }
}
