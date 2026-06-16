package com.bank.db.repository;

import com.bank.db.DatabaseManager;
import com.bank.enums.log.LogType;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class LogRepository {
    private final DatabaseManager db;

    public LogRepository() {
        this.db = DatabaseManager.getInstance();
    }

    public Map<String, Object> findById(Long logId) throws SQLException {
        String sql = "SELECT * FROM logs WHERE id = ?";
        List<Map<String, Object>> rows = db.query(sql, logId);

        if (rows.isEmpty()) {
            return null;
        }
        return rows.get(0);
    }

    public Long create(Map<String, Object> logFields)
            throws SQLException {
        String sql = "INSERT INTO logs " +
                "(customer_id, action, details, status) " + "VALUES (?, ?, ?, ?)";

        List<Map<String, Object>> result = db.query(
                sql,
                logFields.get("customer_id"),
                logFields.get("action"),
                logFields.get("details"),
                logFields.get("status")
        );
        return (Long) result.get(0).get("generated_key");
    }

    public List<Map<String, Object>> findAll()
            throws SQLException {

        String sql = "SELECT * FROM logs ORDER BY created_at DESC";
        return db.query(sql);
    }

    public List<Map<String, Object>> findByUserId(Long customerId)
            throws SQLException {
        String sql = "SELECT * FROM logs " +
                "WHERE customer_id = ? " +
                "ORDER BY created_at DESC";
        return db.query(sql, customerId);
    }

    public List<Map<String, Object>> findByAction(String action)
            throws SQLException {
        String sql = "SELECT * FROM logs " +
                "WHERE action = ? " +
                "ORDER BY created_at DESC";
        return db.query(sql, action);
    }

    public List<Map<String, Object>> findByType(LogType type)
            throws SQLException {
        String sql = "SELECT * FROM logs " +
                "WHERE type = ? " +
                "ORDER BY created_at DESC";
        return db.query(sql, type.name());
    }
}