package com.bank.db.repository;

import com.bank.db.DatabaseManager;
import com.bank.exception.DatabaseOperationException;
import java.sql.SQLException;
import java.util.HashMap;
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

    public Map<String,Object> findFirst(){
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

    public Long insert(Map<String, Object> inboxFields) {
        
        try {
            List<Map<String, Object>> result =db.query(" INSERT INTO inbox( correlation_id,message_type,payload,status,reason) VALUES (?, ?, ?, ?, ?)",
                            inboxFields.get("correlation_id"),
                            inboxFields.get("message_type"),
                            inboxFields.get("payload"),
                            inboxFields.get("status"),
                            inboxFields.get("reason")
                    );

            Object generatedKey =
                    result.get(0).get("generated_key");

            return generatedKey == null
                    ? null
                    : ((Number) generatedKey).longValue();

        } catch (SQLException e) {

            throw new DatabaseOperationException(
                    "Failed to create inbox",
                    e
            );
        }
    }

    public int update(Long id,
                      Map<String, Object> changedFields) {

        try {
            List<Map<String, Object>> result =
                    db.query( "UPDATE inbox SET correlation_id=?,message_type=?,payload=?,status=?,reason=? WHERE id = ?",
                            changedFields.get("correlation_id"),
                            changedFields.get("message_type"),
                            changedFields.get("payload"),
                            changedFields.get("status"),
                            changedFields.get("reason"),
                            id
                    );

            return ((Number) result.get(0).get("affected_rows")).intValue();

        } catch (SQLException e) {

            throw new DatabaseOperationException(
                    "Failed to update inbox with id: " + id,
                    e
            );
        }
    }

    public  Map<String,Object> deleteById(Long id) {

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

            throw new DatabaseOperationException("Failed to delete the inbox",e);
        }
    }

    public List<Map<String, Object>> findAll() {

        try {
            List<Map<String, Object>> rows =
                    db.query(
                            "SELECT * FROM inbox"
                    );

            return rows.isEmpty()
                    ? null
                    : rows;

        } catch (SQLException e) {

            throw new DatabaseOperationException("Failed to retrive inbox",e);
        }
    }
    public List<Map<String,Object>> findAllDepositsMessages() {
        try {
            return  db.query(
                    "SELECT * FROM inbox " +
                            "WHERE message_type = ? " +
                            "ORDER BY created_at DESC",
                            "DEPOSITS"
                    );

        } catch (SQLException e) {
            throw new DatabaseOperationException("Failed to retrieve All deposits" +
                    " messages from inbox",e);
        }
    }
}