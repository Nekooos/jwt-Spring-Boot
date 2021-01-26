package com.practice.jwtapp.service;

import com.practice.jwtapp.testUtil.TestUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
                .thenReturn((testUtil.createTestUser(1L, "user", "password", "user")));

        UserDetails userDetails = jwtUserDetailsService.loadUserByUsername("defaultUser");

        assertEquals("defaultUser", userDetails.getUsername());
        assertEquals("password", userDetails.getPassword());

        verify(userService, Mockito.times(1)).findByUsername("defaultUser");
    }

    @Test
    public void loadByUsernameUsernameNotFoundException() {
        Mockito.when(userService.findByUsername("defaultUser"))
                .thenReturn((testUtil.createTestUser(1L, "user", "password", "user")));

        when(jwtUserDetailsService.loadUserByUsername("defaultUser"))
                .thenThrow(new UsernameNotFoundException("Expected exception"));
        assertThrows(UsernameNotFoundException.class, () ->
            jwtUserDetailsService.loadUserByUsername("defaultUser"));

        verify(userService, Mockito.times(1)).findByUsername("defaultUser");
    }
}
