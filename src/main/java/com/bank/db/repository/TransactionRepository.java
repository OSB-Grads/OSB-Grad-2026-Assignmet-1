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

    public Long insert(Map<String, Object> transactionFields) {

        try {

            List<Map<String, Object>> result =
                    db.query(
                            "INSERT INTO transactions " +
                            "(transaction_id, account_number, dst_account_number, " +
                            "customer_id, transaction_type, amount, status, balance, transaction_date) " +
                            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)",

                            transactionFields.get("transactionId"),
                            transactionFields.get("accountNumber"),
                            transactionFields.get("dstAccountNumber"),
                            transactionFields.get("customerId"),
                            transactionFields.get("transactionType"),
                            transactionFields.get("amount"),
                            transactionFields.get("status"),
                            transactionFields.get("balance"),
                            transactionFields.get("transactionDate")
                    );

            Object generatedKey =
                    result.get(0).get("generated_key");

            return generatedKey == null
                    ? null
                    : ((Number) generatedKey).longValue();

        } catch (SQLException e) {

            throw new DatabaseOperationException(
                    "Failed to create transaction",
                    e
            );
        }
    }

    public int update(Long id, Map<String, Object> changedFields) {

        try {

            List<Map<String, Object>> result =
                    db.query(
                            "UPDATE transactions " +
                            "SET status = ?, balance = ? " +
                            "WHERE id = ?",

                            changedFields.get("status"),
                            changedFields.get("balance"),
                            id
                    );

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