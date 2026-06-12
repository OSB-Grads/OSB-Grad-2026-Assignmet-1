package com.bank.db.repository;

import com.bank.db.DatabaseManager;
import com.bank.exception.DatabaseOperationException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class InboxRepository {
    private final DatabaseManager db;

    public InboxRepository(){
        this.db=DatabaseManager.getInstance();
    }

    public Map<String, Object> findById(Long id) {

        try {
            List<Map<String, Object>> rows =
                    db.query(
                            "SELECT * FROM inbox WHERE id = ?",
                            id
                    );

            return rows.isEmpty()
                    ? null
                    : rows.get(0);

        } catch (SQLException e) {

            throw new DatabaseOperationException("Failed to retrieve Inbox",e);
        }
    }

    public Map<String,Object> findByOne(){
        try {
            List<Map<String, Object>> rows =
                    db.query(
                            "SELECT * FROM inbox LIMIT 1"
                    );

            return rows.isEmpty()
                    ? null
                    : rows.get(0);

        } catch (SQLException e) {

            throw new DatabaseOperationException("Failed to retrieve top inbox ",e);
        }
    }

    public Map<String, Object> deleteById(Long id) {

        try {
            List<Map<String, Object>> rows =
                    db.query(
                            "DELETE FROM inbox WHERE id = ?",
                            id
                    );

            return rows.isEmpty()
                    ? null
                    : rows.get(0);

        } catch (SQLException e) {

            throw new DatabaseOperationException("Failed to retrieve first inbox",e);
        }
    }

    public Map<String, Object> findAll(Long id) {

        try {
            List<Map<String, Object>> rows =
                    db.query(
                            "SELECT * FROM inbox"
                    );

            return rows.isEmpty()
                    ? null
                    : rows.get(0);

        } catch (SQLException e) {

            throw new DatabaseOperationException("Failed to retrieve inbox",e);
        }
    }
}