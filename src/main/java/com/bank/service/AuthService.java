package com.bank.service;
import java.util.HashMap;
import java.util.Map;

import com.bank.db.repository.AuthRepository;
import com.bank.utils.PasswordUtils;

public class AuthService {
    private final AuthRepository authRepository;

    public AuthService() {
        this.authRepository = new AuthRepository();
    }


    public void signup(String username, String password, Long customerId){
        PasswordUtils.validatePassword(password); // calls the utils folder method.
        Map<String, Object> row = new HashMap<>(); // row object for pushing into repo
        row.put("username", username);
        row.put("password_hash", password); //password_hash is just a variable where I'm storing password.
        row.put("customer_id", customerId);
        row.put("role", "CUSTOMER");

        // here we are directly putting the values into repo
        authRepository.insert(row);
    }
}