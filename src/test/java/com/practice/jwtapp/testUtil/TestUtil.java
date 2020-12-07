package com.practice.jwtapp.testUtil;

import com.practice.jwtapp.model.Role;
import com.practice.jwtapp.model.User;
import com.practice.jwtapp.model.UserDataTransferObject;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.HashSet;
import java.util.Set;

public class TestUtil {

    public User createTestUser(String role) {
        User user = new User("defaultUser", createRole(role));
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

    public Set<SimpleGrantedAuthority> createAuthorities() {
        Set<SimpleGrantedAuthority> roles = new HashSet<>();
        roles.add(new SimpleGrantedAuthority("ROLE_USER"));
        roles.add(new SimpleGrantedAuthority("ROLE_EDITOR"));
        roles.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        return roles;
    }

    public Set<Role> createRole(String role) {
        Set<Role> roles = new HashSet<>();

        switch (role) {
            case "user" :
                roles.add(new Role("ROLE_USER"));
                break;
            case "editor" :
                roles.add(new Role("ROLE_EDITOR"));
                break;
            case "admin" :
                roles.add(new Role("ROLE_ADMIN"));
            }

        return roles;
    }
}
