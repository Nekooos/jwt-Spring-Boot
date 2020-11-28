package com.practice.jwtapp.testUtil;

import com.practice.jwtapp.model.Role;
import com.practice.jwtapp.model.User;
import com.practice.jwtapp.model.UserDataTransferObject;

import java.util.HashSet;
import java.util.Set;

public class TestUtil {

    public User createTestUser() {
        Set<Role> roles = new HashSet<>();
        roles.add(createRole("User"));
        User user = new User("defaultUser", roles);
        user.setPassword("password");
        user.setId(9L);
        return user;
    }

    public UserDataTransferObject createUserDto() {
        UserDataTransferObject userDto = new UserDataTransferObject();
        userDto.setUsername("defaultUser");
        userDto.setPassword("password");
        return userDto;
    }

    public Role createRole(String userRole) {
        Role role = new Role();
        role.setRole(userRole);
        return role;
    }
}
