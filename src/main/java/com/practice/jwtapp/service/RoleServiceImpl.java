package com.practice.jwtapp.service;

import com.practice.jwtapp.exception.EntityNotFoundException;
import com.practice.jwtapp.model.Role;
import com.practice.jwtapp.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleRepository roleRepository;

    @Override
    public Role findByName(String name) {
        return roleRepository.findByName(name)
                .orElseThrow(() -> new EntityNotFoundException("Role was not found"));
    }
}

