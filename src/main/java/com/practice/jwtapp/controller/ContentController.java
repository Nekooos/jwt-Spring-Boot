package com.practice.jwtapp.controller;

import com.practice.jwtapp.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    @GetMapping("/pre-admin")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_EDITOR')")
    public String preAuthorizedAdminsAndEditors() {
        return "Pre Authorize for admins aeditorsnd ";
    }

    @GetMapping("/pre-users/{username}")
    @PreAuthorize("#username == authentication.principal.username")
    public String preAuthorizeByRoleAndId(@PathVariable String username) {
        return "Pre authorize by username";
    }

    @GetMapping("/post-users/{username}")
    @PostAuthorize("#username == authentication.principal.username")
    public String postAuthorizeByRoleAndId(@PathVariable String username) {
        return username;
    }

}
