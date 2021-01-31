package com.practice.jwtapp.testUtil;

import com.practice.jwtapp.model.PasswordResetToken;
import com.practice.jwtapp.model.Role;
import com.practice.jwtapp.model.User;
import com.practice.jwtapp.model.UserDto;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class TestUtil {

    public PasswordResetToken createPasswordResetToken(long id, Date date, String token, User user) {
        PasswordResetToken passwordResetToken = new PasswordResetToken();
        passwordResetToken.setId(id);
        passwordResetToken.setExpiryDate(date);
        passwordResetToken.setToken(token);
        passwordResetToken.setUser(user);
        return passwordResetToken;
    }

    public SimpleMailMessage createEmail(String subject, String from, String body, String to) {
        SimpleMailMessage email = new SimpleMailMessage();
        email.setSubject(subject);
        email.setText(body);
        email.setFrom(from);
        email.setTo(to);
        return email;
    }

    public User createTestUser(Long id, String email, String password, String role) {
        User user = new User(false);
        user.setEmail(email);
        user.setPassword(password);
        user.setId(id);
        user.setRoles(createRole(role));
        return user;
    }

    public UserDto createUserDto() {
        UserDto userDto = new UserDto();
        userDto.setEmail("defaultUser");
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
