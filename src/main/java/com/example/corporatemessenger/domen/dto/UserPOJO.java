package com.example.corporatemessenger.domen.dto;

import com.example.corporatemessenger.domen.Role;

import java.util.Set;

public class UserPOJO {
    private Long id;
    private String username;
    private boolean active;
    private Set<Role> roles;
    private String email;

    public UserPOJO(Long id, String username, boolean active, Set<Role> roles, String email) {
        this.id = id;
        this.username = username;
        this.active = active;
        this.roles = roles;
        this.email = email;
    }

    public UserPOJO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    @Override
    public String toString() {
        return "UserPOJO{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", active=" + active +
                ", roles=" + roles +
                '}';
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
