package com.bank.db.repository;

import com.bank.db.DatabaseManager;
import com.bank.exception.DatabaseOperationException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class TransactionRepository {

    private final DatabaseManager db;

    public TransactionRepository() {
        this.db = DatabaseManager.getInstance();
    }

    public Map<String, Object> findByTransactionId(String transactionId) {

        try {

            List<Map<String, Object>> rows =
                    db.query(
                            "SELECT * FROM transactions WHERE transaction_id = ?",
                            transactionId
                    );

            return rows.isEmpty() ? null : rows.get(0);

        } catch (SQLException e) {

            throw new DatabaseOperationException(
                    "Failed to retrieve transaction: " + transactionId,
                    e
            );
        }
    }
    public List<Map<String, Object>> findByCustomerId(Long customerId) {

        try {

            List<Map<String, Object>> rows =
                db.query(
                        "SELECT * FROM transactions WHERE customer_id = ?",
                        customerId
                );

            return rows.isEmpty() ? null : rows;

        } catch (SQLException e) {

            throw new DatabaseOperationException(
                    "Failed to retrieve transactions for customer: " + customerId,
                    e
            );
        }
    }
    public List<Map<String, Object>> findByAccountId(Long accountId) {
        try {
            String sql = "SELECT * FROM transactions WHERE from_account_id = ? OR to_account_id = ?" +
                    "ORDER BY created_at DESC";
            List<Map<String, Object>> rows = db.query(sql);
            return rows.isEmpty() ? null : rows;
        } catch (SQLException e) {
            throw new DatabaseOperationException(
                    "Failed to retrieve transactions for account: " + accountId,
                    e
            );
        }
    }

    public Long insert(Map<String, Object> transactionFields) {

        try {

            List<Map<String, Object>> result =
                    db.query(
                            "INSERT INTO transactions " +
                                    "(customer_id, from_account_id, to_account_id, " +
                                    "transaction_type, amount, description, status) " +
                                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
                                    transactionFields.get("customer_id"),
                                    transactionFields.get("from_account_id"),
                                    transactionFields.get("to_account_id"),
                                    transactionFields.get("transaction_type"),
                                    transactionFields.get("amount"),
                                    transactionFields.get("description"),
                                    transactionFields.get("status"));

            Object generatedKey =
                    result.get(0).get("generated_key");

            return generatedKey == null
                    ? null
                    : ((Number) generatedKey).longValue();

        } catch (SQLException e) {

            throw new DatabaseOperationException("Failed to create transaction", e);
        }
    }

    public int update(Long id, Map<String, Object> changedFields) {

        try {

            List<Map<String, Object>> result =
                    db.query("UPDATE transactions " +
                                    "SET status = ? " +
                                    "updated_at = CURRENT_TIMESTAMP," +
                                    "WHERE id = ?",
                                    changedFields.get("status"), id);

            return ((Number) result.get(0)
                    .get("affected_rows"))
                    .intValue();

        } catch (SQLException e) {

            throw new DatabaseOperationException(
                    "Failed to update transaction with id: " + id,
                    e
            );
        }
    }

    public List<Map<String, Object>> findAll() {

        try {

            return db.query(
                    "SELECT * FROM transactions"
            );

        } catch (SQLException e) {

            throw new DatabaseOperationException(
                    "Failed to retrieve all transactions",
                    e
            );
        }
    }
}