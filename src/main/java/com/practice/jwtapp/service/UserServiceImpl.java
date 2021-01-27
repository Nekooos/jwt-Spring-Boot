package com.practice.jwtapp.service;

import com.practice.jwtapp.exception.UserNotFoundException;
import com.practice.jwtapp.exception.UsernameExistsException;
import com.practice.jwtapp.model.PasswordResetToken;
import com.practice.jwtapp.model.Role;
import com.practice.jwtapp.model.User;
import com.practice.jwtapp.model.UserDto;
import com.practice.jwtapp.repository.PasswordResetTokenRepository;
import com.practice.jwtapp.repository.RoleRepository;
import com.practice.jwtapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    PasswordResetTokenRepository passwordResetTokenRepository;
    @Autowired
    PasswordResetTokenService passwordResetTokenService;

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username + " not found"));
    }

    @Override
    public User saveUser(UserDto userDto) {
        boolean usernameExists = userRepository.existsByUsername(userDto.getUsername());

        if(!usernameExists) {
            User user = new User();
            user.setUsername(userDto.getUsername());
            user.setPassword(passwordEncoder.encode(userDto.getPassword()));
            user.setRoles(addRoleToUser());
            return userRepository.save(user);
        } else {
            throw new UsernameExistsException("Email is not unique");
        }

    }

    private Set<Role> addRoleToUser() {
        Set<Role> roles = new HashSet<>();
        Role role = roleRepository.findByName("USER");
        roles.add(role);
        return roles;
    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User was not found"));
    }

    @Override
    public void resetPassword(String username) {
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("User was not found"));

        PasswordResetToken passwordResetToken = passwordResetTokenService.createPasswordResetToken(user);
        passwordResetTokenRepository.save(passwordResetToken);

    }
}
