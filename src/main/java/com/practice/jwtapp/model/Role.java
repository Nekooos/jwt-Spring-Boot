package com.practice.jwtapp.model;

import javax.persistence.*;

@Entity
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private int roleId;
    @Column(name = "role_name")
    private String name;

    public Role() {

    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public Role(String role) {
        this.name = role;
    }

    public String getRole() {
        return name;
    }

    public void setRole(String role) {
        this.name = role;
    }
}
