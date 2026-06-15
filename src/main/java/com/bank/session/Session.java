package com.bank.session;

import com.bank.enums.Role;

public class Session {

    private static Session instance;

    private Long customerId;
    private Role role;

    private Session() {
    }

    public static Session getInstance() {
        if (instance == null) {
            instance = new Session();
        }
        return instance;
    }

    public void login(Long customerId, Role role) {
        this.customerId = customerId;
        this.role = role;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public Role getRole() {
        return role;
    }

    public void logout() {
        customerId = null;
        role = null;
    }
}