package com.bank.db.repository;

import com.bank.db.DatabaseManager;
import com.bank.exception.DatabaseOperationException;
import com.bank.utils.UuidGeneratorUtil;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class AuthRepository {

    private final DatabaseManager db;

    public AuthRepository() {
        this.db = DatabaseManager.getInstance();
    }

    public Map<String, Object> findById(String id) {

        try {

            String sql =
                    "SELECT * FROM auth WHERE id = ?";

            List<Map<String, Object>> results =
                    db.query(sql, id);

            if (results == null || results.isEmpty()) {
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
                    "SELECT * FROM auth WHERE username = ?";

            List<Map<String, Object>> results =
                    db.query(sql, username);

            if (results == null || results.isEmpty()) {
                return null;
            }

            return results.get(0);

        } catch (Exception e) {
            throw new RuntimeException(
                    "Failed to find auth user by username", e
            );
        }
    }

    public String insert(Map<String, Object> authFields) {

        try {
            String authId = UuidGeneratorUtil.generateUuid();
            authFields.put("id",authId);

            String sql =
                    "INSERT INTO auth " +
                            "(id, username, password_hash, role) " +
                            "VALUES (?, ?, ?, ?)";

            List<Map<String, Object>> authRow =
                    db.query(
                            sql,
                            authFields.get("id"),
                            authFields.get("username"),
                            authFields.get("password_hash"),
                            authFields.get("role")
                    );
            return authId;

        } catch (SQLException e) {
            throw new DatabaseOperationException(
                    "Failed to create auth user", e);
        }
    }
}