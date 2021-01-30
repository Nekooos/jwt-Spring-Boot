package com.practice.jwtapp.controller;

import com.practice.jwtapp.model.PasswordDto;
import com.practice.jwtapp.model.User;
import com.practice.jwtapp.model.UserDto;
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

@RestController
@RequestMapping(value = "/user")
public class UserController {
    Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;
    @Autowired
    private PasswordResetTokenService passwordResetTokenService;

    @PostMapping("/user/resetPassword")
    public ResponseEntity<?> resetPassword(HttpServletRequest request, @RequestParam("username") String username) {
        User user = userService.resetPassword(username);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/user/changePassword")
    public ResponseEntity<?> ShowPasswordPageIfValidToken(@RequestParam("token") String token) {
        passwordResetTokenService.validatePasswordResetToken(token);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/user/changePassword");
        return ResponseEntity.ok().headers(headers).build();
    }

    @PostMapping("/user/savePassword")
    public ResponseEntity<?> savePassword(@Valid PasswordDto passwordDto) {
        passwordResetTokenService.validatePasswordResetToken(passwordDto.getToken());

        User user = userService.changePassword(passwordDto.getToken());
        return ResponseEntity.ok(user);
    }

    @PostMapping("/save")
    public ResponseEntity<?> saveUser(@Valid @RequestBody UserDto userDto) {
        User user = userService.saveUser(userDto);
        return ResponseEntity.ok(user);

    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        User user = userService.findById(id);
        return ResponseEntity.ok(user);
    }
}
