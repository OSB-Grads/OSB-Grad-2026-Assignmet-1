package com.bank.dto;
import com.bank.enums.Role;
public class AuthUserDTO {

    private String id;
    private String username;
    private String passwordHash;
    private String customerId;
    private Role role;

    public AuthUserDTO() {
    }

    public AuthUserDTO(String id,
                       String username,
                       String passwordHash,
                       String customerId,
                       Role role) {
        this.id = id;
        this.username = username;
        this.passwordHash = passwordHash;
        this.customerId = customerId;
        this.role = role;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
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