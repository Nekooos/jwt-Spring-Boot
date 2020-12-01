package com.practice.jwtapp.controller;

import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class ContentController {

    @GetMapping("/")
    public String getStartPage() {
        return "Everyone can reach this";
    }

    @GetMapping("/logged-in")
    @Secured("USER")
    public String getLoggedInPage() {
        return "logged in users can reach this";
    }

    @GetMapping("/user")
    @Secured("ADMIN")
    public String getContentForLoggedInUsers() {
        return "Users and admins can reach this";
    }

    @GetMapping("/admin")
    public String getContentForLoggedInAdmins() {
        return "Users can reach this";
    }
}
