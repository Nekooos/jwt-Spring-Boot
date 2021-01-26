package com.practice.jwtapp.service;

import com.practice.jwtapp.model.User;
import com.practice.jwtapp.model.UserDto;

public interface UserService {
    User findByUsername(String username);

    User saveUser(UserDto user);

    User findById(Long id);
}
