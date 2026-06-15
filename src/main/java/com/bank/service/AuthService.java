package com.bank.service;
import java.util.HashMap;
import java.util.Map;

import com.bank.db.repository.AuthRepository;
import com.bank.exception.DatabaseOperationException;
import com.bank.exception.UserCreationException;
import com.bank.utils.ValidationUtils;

public class AuthService {
    private final AuthRepository authRepository;

    public AuthService() {
        this.authRepository = new AuthRepository();
    }

    public Long signup(String username, String password) throws DatabaseOperationException, UserCreationException {
        ValidationUtils.validatePassword(password); // calls the utils folder method.
        Map<String, Object> row = new HashMap<>(); // row object for pushing into repo
        row.put("username", username);
        row.put("password_hash", password); //password_hash is just a variable where I'm storing password.
        row.put("role", "CUSTOMER");
        // here we are directly putting the values into repo
        return authRepository.insert(row);
    }

}