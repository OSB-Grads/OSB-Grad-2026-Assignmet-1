package com.bank.db.repository;

import com.bank.db.DatabaseManager;
import com.bank.dto.AuthUserDTO;
import com.bank.mapper.AuthMapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class AuthRepository {

    private final DatabaseManager db;

    public AuthRepository() {
        this.db = DatabaseManager.getInstance();
    }

    public Map<String, Object> findById(BigDecimal id) {

        try {

            String sql =
                    "SELECT * FROM auth_users WHERE id = ?";

            List<Map<String, Object>> results =
                    db.query(sql, id);

            if (results.isEmpty()) {
                return null;
            }

            return results.get(0);

        } catch (Exception e) {
            throw new RuntimeException(
                    "Failed to find auth user by id", e
            );
        }
    }

    public Map<String, Object> findByUsername(String username) {

        try {

            String sql =
                    "SELECT * FROM auth_users WHERE username = ?";

            List<Map<String, Object>> results =
                    db.query(sql, username);

            if (results.isEmpty()) {
                return null;
            }

            return results.get(0);

        } catch (Exception e) {
            throw new RuntimeException(
                    "Failed to find auth user by username", e
            );
        }
    }

    public void insert(AuthUserDTO dto) {

        try {

            String sql =
                    "INSERT INTO auth_users " +
                            "(username, password_hash, customer_id, role) " +
                            "VALUES (?, ?, ?, ?)";

            db.query(
                    sql,
                    dto.getUsername(),
                    dto.getPasswordHash(),
                    dto.getCustomerId(),
                    dto.getRole().name()
            );

        } catch (Exception e) {
            throw new RuntimeException(
                    "Failed to insert auth user", e
            );
        }
    }

    public AuthUserDTO findDtoByUsername(String username) {

        Map<String, Object> row =
                findByUsername(username);

        return AuthMapper.toDTO(row);
    }
    // repo --> mapper---> dto
    public AuthUserDTO findDtoById(BigDecimal id) {

        Map<String, Object> row =
                findById(id);

        return AuthMapper.toDTO(row);
    }
}