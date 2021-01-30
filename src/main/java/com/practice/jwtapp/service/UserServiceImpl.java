package com.practice.jwtapp.service;

import com.practice.jwtapp.exception.EmailExistsException;
import com.practice.jwtapp.exception.PasswordResetTokenNotValidException;
import com.practice.jwtapp.exception.UserNotFoundException;
import com.practice.jwtapp.model.*;
import com.practice.jwtapp.repository.PasswordResetTokenRepository;
import com.practice.jwtapp.repository.RoleRepository;
import com.practice.jwtapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

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
    @Autowired
    EmailService emailService;

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(email + " not found"));
    }

    @Override
    public User saveUser(UserDto userDto) {
        boolean usernameExists = userRepository.existsByEmail(userDto.getEmail());

        if(!usernameExists) {
            User user = createUserFromUserDto(userDto);
            return userRepository.save(user);
        } else {
            throw new EmailExistsException("Email is not unique");
        }

    }

    private User createUserFromUserDto(UserDto userDto) {
        User user = new User();
        user.setEmail(userDto.getEmail());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setRoles(addRoleToUser());
        return user;
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
                .orElseThrow(() -> new UserNotFoundException("User was not found"));
    }

    @Override
    public User resetPassword(String email, String url) {
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("User was not found"));

        PasswordResetToken passwordResetToken = passwordResetTokenService.createPasswordResetToken(user);
        passwordResetTokenService.savePasswordResetToken(passwordResetToken);

        String path = emailService.createResetUrl(passwordResetToken.getToken(), url);
        SimpleMailMessage simpleMailMessage = emailService.createEmail("Change password", path, user);
        emailService.sendMail(simpleMailMessage);

        return user;
    }

    public User saveNewPassword(PasswordDto passwordDto, String email) {
        User user = findByEmail(email);
        if(user.getPassword().equals(passwordDto.getOldPassword())) {
            user.setPassword(passwordDto.getNewPassword());
        } else {
            throw new PasswordResetTokenNotValidException("Old password does not match");
        }
        return userRepository.save(user);
    }
}
