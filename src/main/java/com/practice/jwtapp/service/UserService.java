package com.practice.jwtapp.service;

import com.practice.jwtapp.model.PasswordDto;
import com.practice.jwtapp.model.User;
import com.practice.jwtapp.model.UserDto;

import java.util.List;

public interface UserService {
    User findByEmail(String email);

    User saveUser(UserDto user);

    User findById(Long id);

    User addRole(long id, String roleName);

    User resetPassword(String email);

    User saveNewPassword(PasswordDto passwordDto, String email);

    User confirmAccount(User user);

    User enableAccount(String token);

    List<User> getAll();

    String getUserAuthenticatedInformation(String email);
}
