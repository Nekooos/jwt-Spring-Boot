package com.practice.jwtapp.service;

import com.practice.jwtapp.model.PasswordDto;
import com.practice.jwtapp.model.User;
import com.practice.jwtapp.model.UserDto;

public interface UserService {
    User findByEmail(String email);

    User saveUser(UserDto user);

    User findById(Long id);

    User resetPassword(String email, String url);

    User saveNewPassword(PasswordDto passwordDto, String email);
}
