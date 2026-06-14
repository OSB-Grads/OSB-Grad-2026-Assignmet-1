package com.bank.db.repository;

import com.bank.db.DatabaseManager;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class AuthRepository {

    private final DatabaseManager db;

    public AuthRepository() {
        this.db = DatabaseManager.getInstance();
    }

    public Map<String, Object> findById(Long id) {

        try {

            String sql =
                    "SELECT * FROM auth_users WHERE id = ?";

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
                    "SELECT * FROM auth_users WHERE username = ?";

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

    public Long insert(Map<String, Object> authFields) throws SQLException {

        String sql =
                "INSERT INTO auth_users " +
                        "(username, password_hash, customer_id, role) " +
                        "VALUES (?, ?, ?, ?)";

        List<Map<String, Object>> authRow =
                db.query(
                        sql,
                        authFields.get("username"),
                        authFields.get("password_hash"),
                        authFields.get("customer_id"),
                        authFields.get("role")
                );

        return (Long) authRow.get(0).get("id");
    }
}