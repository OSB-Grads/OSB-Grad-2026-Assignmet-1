package com.bank.session;

import com.bank.enums.Role;

public class Session {

    private static Session instance;

    private String customerId;
    private Role role;
    private Session() {
    }

    public static Session getInstance() {
        if (instance == null) {
            instance = new Session();
        }
        return instance;
    }

    public void login(String customerId, Role role) {
        this.customerId = customerId;
        this.role = role;
    }

    public String getCustomerId() {
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