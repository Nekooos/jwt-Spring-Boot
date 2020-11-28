package com.practice.jwtapp.controller;

import com.practice.jwtapp.model.User;
import com.practice.jwtapp.model.UserDataTransferObject;
import com.practice.jwtapp.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/user")
public class UserController {
    Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @PostMapping("/save")
    public ResponseEntity<?> saveUser(@RequestBody UserDataTransferObject userDto) {
        User user = userService.saveUser(userDto);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        User user = userService.findById(id);
        return ResponseEntity.ok(user);
    }
}
