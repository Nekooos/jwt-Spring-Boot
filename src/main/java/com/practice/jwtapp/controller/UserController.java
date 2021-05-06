package com.practice.jwtapp.controller;

import com.practice.jwtapp.model.PasswordDto;
import com.practice.jwtapp.model.User;
import com.practice.jwtapp.model.UserDto;
import com.practice.jwtapp.service.ConfirmAccountService;
import com.practice.jwtapp.service.PasswordResetTokenService;
import com.practice.jwtapp.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/user")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;
    @Autowired
    private PasswordResetTokenService passwordResetTokenService;
    @Autowired
    private ConfirmAccountService confirmAccountService;

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody String username) {
        User user = userService.resetPassword(username);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/change-password-page")
    public ResponseEntity<?> ShowPasswordPageIfValidToken(@RequestParam("token") String token) {
        passwordResetTokenService.validatePasswordResetToken(token);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/user/changePassword");
        return ResponseEntity.ok().headers(headers).build();
    }

    @PostMapping("/save-new-password")
    public ResponseEntity<?> savePassword(@Valid @RequestBody PasswordDto passwordDto, @RequestParam("email") String email) {
        passwordResetTokenService.validatePasswordResetToken(passwordDto.getToken());

        User updatedUser = userService.saveNewPassword(passwordDto, email);
        return ResponseEntity.ok(updatedUser);
    }

    @PostMapping("/save")
    public ResponseEntity<?> saveUser(@Valid @RequestBody UserDto userDto) {
        User user = userService.saveUser(userDto);
        User savedUser = userService.confirmAccount(user);
        return ResponseEntity.ok(savedUser);
    }

    @GetMapping("/confirm-account-redirect")
    public ResponseEntity<?> redirectIfValidToken(@RequestParam("token") String token) {
        confirmAccountService.validateConfirmAccountToken(token);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/user/account-enabled?" + token);
        return ResponseEntity.ok().headers(headers).build();
    }

    @PostMapping("/enable-account")
    public ResponseEntity<?> enableAccount(@RequestBody String token) {
        confirmAccountService.validateConfirmAccountToken(token);

        User user = userService.enableAccount(token);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        User user = userService.findById(id);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/add-role")
    public ResponseEntity<?> changeUserRole(@RequestParam("id") long id, @RequestParam("role") String role) {
        User modifiedUser = userService.addRole(id, role);
        return ResponseEntity.ok(modifiedUser);
    }

    @GetMapping("/get-all")
    public ResponseEntity<?> getAllUsers() {
        List<User> users = userService.getAll();
        return ResponseEntity.ok(users);
    }
}
