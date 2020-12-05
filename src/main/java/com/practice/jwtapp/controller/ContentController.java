package com.practice.jwtapp.controller;

import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
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
    public String getLoggedInPage() {
        return "logged in users can reach this";
    }

    @GetMapping("/users")
    @Secured({"ROLE_USER", "ROLE_EDITOR", "ROLE_ADMIN"})
    public String getContentForLoggedInUsers() {
        return "Users, editors and admins reach this";
    }

    @GetMapping("/editors")
    @Secured({"ROLE_EDITOR", "ROLE_ADMIN"})
    public String getContentForLoggedInEditors() {
        return "Editors and admins can reach this";
    }

    @GetMapping("/admins")
    @Secured("ROLE_ADMIN")
    public String getContentForLoggedInAdmins() {
        return "Admin can reach this";
    }

    @GetMapping("/none")
    @Secured("ROLE_NONE")
    public String getContentForNoOne() {
        return "none can reach this";
    }
}
