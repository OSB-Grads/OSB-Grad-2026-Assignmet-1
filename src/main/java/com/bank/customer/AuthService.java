package com.bank.customer;

import com.bank.db.repository.AuthRepository;
import com.bank.dto.AuthUserDTO;
import com.bank.enums.log.LogType;
import com.bank.mapper.AuthMapper;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class AuthService {

    private final AuthRepository repository;
    private final LoggerService loggerService;

    public AuthService() {
        this.repository = new AuthRepository();
        this.loggerService = new LoggerService();
    }

    public Map<String, Object> login(String username, String password)
            throws SQLException {

        if (username == null || username.trim().isEmpty()) {

            loggerService.log(
                    null,
                    "LOGIN",
                    "Username is empty",
                    LogType.FAILURE
            );

            throw new RuntimeException("username is empty");
        }

        if (password == null || password.trim().isEmpty()) {

            loggerService.log(
                    null,
                    "LOGIN",
                    "Password is empty",
                    LogType.FAILURE
            );

            throw new RuntimeException("Passwor is  empty");
        }

        Map<String, Object> userInfo = repository.findByUsername(username);

        if (userInfo == null || userInfo.isEmpty()) {

            loggerService.log(
                    null,
                    "LOGIN",
                    "User not found",
                    LogType.FAILURE
            );

            throw new RuntimeException("Invalid");
        }

        AuthUserDTO dto = AuthMapper.toDTO(userInfo);

        if (password.equals(dto.getPasswordHash())) {

            Map<String, Object> result = new HashMap<>();
            result.put("authId", dto.getId());
            result.put("role", dto.getRole());

            loggerService.log(
                    dto.getId(),
                    "LOGIN",
                    "User logged in successfully",
                    LogType.SUCCESS
            );

            return result;

        } else {

            loggerService.log(
                    dto.getId(),
                    "LOGIN",
                    "Invalid password",
                    LogType.FAILURE
            );

            throw new RuntimeException("Invalid username or password");
        }
    }
}