package com.bank.service;

import com.bank.db.repository.AuthRepository;
// import com.bank.dto.AuthUserDTO;
// import com.bank.enums.Role;
// import com.bank.mapper.AuthMapper;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class AuthService {
    private final AuthRepository authRepository;

    public AuthService(){
        this.authRepository = new AuthRepository();
    }
    public void signup(String username, String password, Long customerId) throws SQLException {
        Map<String, Object> exist = authRepository.findByUsername(username);
        if(exist!=null){
            throw new RuntimeException("Username already exists");
        }
        validatePassword(password);
        Map<String, Object> row = new HashMap<>(); // row object for pushing into repo
        row.put("username", username);
        row.put("password_hash", password); //password_hash is just a variable where im storing password.
        row.put("customer_id", customerId);
        row.put("role", "CUSTOMER");

        // here we are directly putting the values into repo
        authRepository.insert(row);
    }

    private void validatePassword(String password) {

        boolean valid =
                password != null &&
                        password.length() >= 8 &&
                        password.matches(".*[A-Z].*") &&
                        password.matches(".*[a-z].*") &&
                        password.matches(".*\\d.*");

        if (!valid) {
            throw new RuntimeException(
                    "Password must be at least 8 characters long and contain uppercase, lowercase and a digit");
        }
    }
}