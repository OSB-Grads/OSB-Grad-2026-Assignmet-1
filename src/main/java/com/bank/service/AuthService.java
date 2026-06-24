package com.bank.service;
import java.util.HashMap;
import java.util.Map;
import com.bank.customer.LoggerService;
import com.bank.utils.UuidGeneratorUtil;
import com.bank.db.repository.AuthRepository;

import com.bank.enums.log.LogType;
import com.bank.exception.DatabaseOperationException;
import com.bank.exception.UserCreationException;
import com.bank.utils.PasswordUtil;
import com.bank.utils.ValidationUtils;

public class AuthService {
    private final AuthRepository authRepository;
    private final LoggerService loggerService;

    public AuthService() {
        this.authRepository = new AuthRepository();
        this.loggerService = new LoggerService();

    }

    public String signup(String username, String password) throws DatabaseOperationException, UserCreationException {
        try {
            ValidationUtils.validatePassword(password); // calls the utils folder method.

            Map<String, Object> row = new HashMap<>(); // row object for pushing into repo
            row.put("username", username);
            row.put("password_hash", PasswordUtil.hashPassword(password)); //password_hash is just a variable where I'm storing password.
            row.put("role", "CUSTOMER");
            String id = authRepository.insert(row);

            loggerService.log(
                    "SIGNUP",
                    "Customer account created for username: " + username,
                    LogType.SUCCESS
            );
            return id;

        } catch (UserCreationException e) {
            loggerService.log(
                    "SIGNUP",
                    "Customer signup rejected for username: " + username,

                    LogType.FAILURE);
            throw e;
        } catch (DatabaseOperationException e) {
            loggerService.log(
                    "SIGNUP",
                    "Database error during signup for username: " + username,
                    LogType.ERROR);
            throw e;

        }
    }
}