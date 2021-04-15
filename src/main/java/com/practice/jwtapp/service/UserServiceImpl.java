package com.practice.jwtapp.service;

import com.practice.jwtapp.exception.EmailExistsException;
import com.practice.jwtapp.exception.AccountTokenNotValidException;
import com.practice.jwtapp.exception.EntityNotFoundException;
import com.practice.jwtapp.exception.UserNotFoundException;
import com.practice.jwtapp.model.*;
import com.practice.jwtapp.repository.RoleRepository;
import com.practice.jwtapp.repository.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {
    private static final String USER_NOT_FOUND_MESSAGE = "User was not found";
    private static final String CONFIRM_ACCOUNT_URL = "user/confirm-account";
    private static final String CHANGE_PASSWORD_URL = "user/change-password";
    private static final String ROLE_NOT_FOUND_MESSAGE = "Role was not found";

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordResetTokenService passwordResetTokenService;
    @Autowired
    private EmailService emailService;
    @Autowired
    private ConfirmAccountService confirmAccountService;

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(USER_NOT_FOUND_MESSAGE));
    }

    @Override
    public User saveUser(UserDto userDto) {
        boolean usernameExists = userRepository.existsByEmail(userDto.getEmail());

        if(!usernameExists) {
            User user = createUserFromUserDto(userDto);
            userRepository.save(user);
            return user;
        } else {
            throw new EmailExistsException("Email is already in use");
        }
    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND_MESSAGE));
    }

    @Override
    public User addRole(long id, String roleName) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND_MESSAGE));
        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new EntityNotFoundException(ROLE_NOT_FOUND_MESSAGE));
        User modifiedUser = addRole(user, role);
        return userRepository.save(modifiedUser);
    }

    private User addRole(User user, Role role) {
        Set<Role> roles = user.getRoles();
        roles.add(role);
        user.setRoles(roles);
        return user;
    }

    @Override
    public User resetPassword(String email) {
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException(USER_NOT_FOUND_MESSAGE));

        PasswordResetToken passwordResetToken = passwordResetTokenService.createPasswordResetToken(user);
        passwordResetTokenService.savePasswordResetToken(passwordResetToken);

        String path = emailService.createResetUrl(passwordResetToken.getToken(), CHANGE_PASSWORD_URL);
        SimpleMailMessage simpleMailMessage = emailService.createEmail("Change password", path, user);
        emailService.sendMail(simpleMailMessage);

        return user;
    }

    public User saveNewPassword(PasswordDto passwordDto, String email) {
        User user = findByEmail(email);
        if(user.getPassword().equals(passwordDto.getOldPassword())) {
            user.setPassword(passwordDto.getNewPassword());
        } else {
            throw new AccountTokenNotValidException("Old password does not match new");
        }
        return userRepository.save(user);
    }

    @Override
    public User confirmAccount(User user) {
        ConfirmAccountToken confirmAccountToken = confirmAccountService.createConfirmAccountToken(user);
        ConfirmAccountToken savedConfirmAccountToken = confirmAccountService.saveConfirmAccountToken(confirmAccountToken);

        String path = emailService.createResetUrl(savedConfirmAccountToken.getToken(), CONFIRM_ACCOUNT_URL);
        SimpleMailMessage simpleMailMessage = emailService.createEmail("Confirm Account", path, user);
        emailService.sendMail(simpleMailMessage);

        return user;
    }

    @Override
    public User enableAccount(String token) {
        ConfirmAccountToken confirmAccountToken = confirmAccountService.findConfirmAccountTokenByToken(token);
        User user = confirmAccountToken.getUser();
        user.setEnabled(true);
        return userRepository.save(user);
    }

    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }

    private User createUserFromUserDto(UserDto userDto) {
        User user = new User(false);
        user.setEmail(userDto.getEmail());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setRoles(addRoleToUser());
        return user;
    }

    private Set<Role> addRoleToUser() {
        Set<Role> roles = new HashSet<>();
        Role role = roleRepository.findByName("USER")
                .orElseThrow(() -> new EntityNotFoundException("Role was not found"));
        roles.add(role);
        return roles;
    }
}
