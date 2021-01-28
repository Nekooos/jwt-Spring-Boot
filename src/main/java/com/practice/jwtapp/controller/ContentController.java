package com.practice.jwtapp.controller;

import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return "logged in users can reach this " + authentication;
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
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String getContentForLoggedInAdmins() {
        return "Admin can reach this";
    }

    @GetMapping("/pre-admin-or-editor")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_EDITOR')")
    public String preAuthorizedAdminsAndEditors() {
        return "Pre Authorize for admins and editors ";
    }

    @GetMapping("/pre-users")
    @PreAuthorize("#email == authentication.principal.username")
    public String preAuthorizeByEmail(@PathVariable String username) {
        return "Pre authorize by email: " + username;
    }

    //TODO
    @GetMapping("/post-users/{email}")
    @PostAuthorize("#email == authentication.principal.username")
    public String postAuthorizeByRoleAndId(@PathVariable String username) {
        return username;
    }

    @PreAuthorize("ROLE_ADMIN")
    public String admins() {
        return "for admins";
    }

}
