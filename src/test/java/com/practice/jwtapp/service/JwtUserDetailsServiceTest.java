package com.practice.jwtapp.service;

import com.practice.jwtapp.testUtil.TestUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class JwtUserDetailsServiceTest {
    @Mock
    private UserService userService;
    @InjectMocks
    private JwtUserDetailsService jwtUserDetailsService;
    private TestUtil testUtil;

    @BeforeEach
    public void setup() {
        jwtUserDetailsService = new JwtUserDetailsService();
        testUtil = new TestUtil();
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void loadByUsername() {
        Mockito.when(userService.findByUsername("defaultUser"))
                .thenReturn((testUtil.createTestUser("user")));

        UserDetails userDetails = jwtUserDetailsService.loadUserByUsername("defaultUser");

        Assertions.assertEquals("defaultUser", userDetails.getUsername());
        Assertions.assertEquals("password", userDetails.getPassword());

        Mockito.verify(userService, Mockito.times(1)).findByUsername("defaultUser");
    }

    @Test
    public void loadByUsernameUsernameNotFoundException() {
        Mockito.when(userService.findByUsername("defaultUser"))
                .thenReturn((testUtil.createTestUser("user")));

        Mockito.when(jwtUserDetailsService.loadUserByUsername("defaultUser"))
                .thenThrow(new UsernameNotFoundException("Expected exception"));
        Assertions.assertThrows(UsernameNotFoundException.class, () ->
            jwtUserDetailsService.loadUserByUsername("defaultUser"));

        Mockito.verify(userService, Mockito.times(1)).findByUsername("defaultUser");
    }
}
