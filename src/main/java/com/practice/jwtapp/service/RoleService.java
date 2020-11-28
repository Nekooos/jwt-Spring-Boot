package com.practice.jwtapp.service;

import com.practice.jwtapp.model.Role;

public interface RoleService {

    Role findByName(String name);

}
