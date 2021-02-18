package com.practice.jwtapp.service;

import com.practice.jwtapp.exception.EntityNotFoundException;
import com.practice.jwtapp.model.Role;
import com.practice.jwtapp.repository.RoleRepository;
import com.practice.jwtapp.testUtil.TestUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class RoleServiceTest {
    @InjectMocks
    private RoleServiceImpl roleService;
    @Mock
    private RoleRepository roleRepository;

    private TestUtil testUtil;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        testUtil = new TestUtil();
    }

    @Test
    public void findByName() {
        Role role = testUtil.createRole();

        when(roleRepository.findByName(anyString()))
                .thenAnswer(function -> Optional.of(role));

        Role foundRole = roleService.findByName("ADMIN");

        assertEquals("ADMIN", foundRole.getRole());

        verify(roleRepository).findByName(anyString());
    }

    @Test
    public void findByNameThrowsRoleNotFoundExceptionTest() {
        assertThrows(EntityNotFoundException.class, () -> {
            roleService.findByName("ADMIN");
        });

        verify(roleRepository).findByName(anyString());
    }
}
