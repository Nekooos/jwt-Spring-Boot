package com.practice.jwtapp.testUtil;

import com.practice.jwtapp.model.Role;
import com.practice.jwtapp.model.User;
import com.practice.jwtapp.model.UserDto;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.HashSet;
import java.util.Set;

public class TestUtil {

    public User createTestUser(Long id, String username, String password, String role) {
        User user = new User();
        user.setEmail(username);
        user.setPassword(password);
        user.setId(id);
        user.setRoles(createRole(role));
        return user;
    }

    public UserDto createUserDto() {
        UserDto userDto = new UserDto();
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
                break;
            }

        return roles;
    }
}
