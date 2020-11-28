package com.practice.jwtapp.service;

import com.practice.jwtapp.model.User;
import com.practice.jwtapp.model.UserDataTransferObject;

public interface UserService {
    User findByUsername(String username);

    User saveUser(UserDataTransferObject user);

    User findById(Long id);
}
