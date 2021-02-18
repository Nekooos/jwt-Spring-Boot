package com.practice.jwtapp.service;

import org.springframework.security.access.annotation.Secured;

public class ContentServiceImpl {
    @Secured("ROLE_ADMIN")
    public String adminsAllowed() {
        return "ContentService admins alowed";
    }

}
