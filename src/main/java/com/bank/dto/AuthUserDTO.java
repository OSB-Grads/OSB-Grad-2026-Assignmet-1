package com.bank.dto;

import com.bank.enums.Role;
//import java.math.BigDecimal;

public class AuthUserDTO {

    private long id;
    private String username;
    private String passwordHash;
    private long customerId;
    private Role role;

    public AuthUserDTO() {
    }

    public AuthUserDTO(long id,
                       String username,
                       String passwordHash,
                       long customerId,
                       Role role) {
        this.id = id;
        this.username = username;
        this.passwordHash = passwordHash;
        this.customerId = customerId;
        this.role = role;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(long customerId) {
        this.customerId = customerId;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "AuthUserDTO{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", customerId=" + customerId +
                ", role=" + role +
                '}';
    }
}