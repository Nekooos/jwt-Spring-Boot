package com.practice.jwtapp.controller;

import com.practice.jwtapp.config.JwtRequestFilter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ContextConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ContentControllerSecurityTest {

    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private JwtRequestFilter jwtRequestFilter;
    @MockBean
    private ContentController contentController;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .build();
    }

    @Test
    @WithMockUser
    @DisplayName("get /logged-in authenticated")
    public void loggedInAllowed() throws Exception {
        this.mockMvc
                .perform(get("/logged-in"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(authenticated());
    }

    @Test
    @WithMockUser(roles = "USER")
    public void usersAllowed() throws Exception {
        this.mockMvc
                .perform(get("/users"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(authenticated());
    }

    @Test
    @WithMockUser(roles = "EDITOR")
    public void editorsAllowed() throws Exception {
        this.mockMvc
                .perform(get("/editors"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(authenticated().withRoles("EDITOR"));
    }

    @Test
    @WithMockUser(roles = "USER")
    public void userNotAllowed() throws Exception {
        this.mockMvc
                .perform(get("/editors"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(authenticated().withRoles("USER"));
    }

    @Test
    @WithMockUser(authorities="ROLE_ADMIN")
    public void adminsAllowed() throws Exception {
        this.mockMvc
                .perform(get("/admins"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(authenticated());
    }

}
