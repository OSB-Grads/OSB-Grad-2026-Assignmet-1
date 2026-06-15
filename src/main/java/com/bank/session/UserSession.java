package com.bank.session;

import com.bank.enums.Role;

public class UserSession {

    private static UserSession instance; // object for us

    private Long customerId;
    private Role role;

    private UserSession() {}
    // returns the instance
    public static UserSession getInstance() {
        if (instance == null) {
            instance = new UserSession();
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
}