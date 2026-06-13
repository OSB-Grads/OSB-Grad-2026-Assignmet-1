package com.bank.db.repository;

import com.bank.db.DatabaseManager;
import com.bank.exception.DatabaseOperationException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class AccountRepository {

    private final DatabaseManager db;

    public AccountRepository() {
        this.db = DatabaseManager.getInstance();
    }

    public Map<String, Object> findAccountsById(Long id) {

        try {

            List<Map<String, Object>> row = db.query(
                    "SELECT * FROM accounts WHERE id = ?",
                    id);

            return row.isEmpty() || row == null ? null : row.get(0);

        } catch (SQLException e) {

            throw new DatabaseOperationException("Failed to retrieve account", e);
        }
    }

    public Map<String, Object> findProductByAccountNumber(String accountNumber) {

        try {

            List<Map<String, Object>> row = db.query(
                    "SELECT * FROM accounts WHERE account_number = ?",
                    accountNumber);

            return row.isEmpty() || row == null ? null : row.get(0);

        } catch (SQLException e) {

            throw new DatabaseOperationException("Failed to retrieve account with account number: " + accountNumber, e);
        }
    }

    public List<Map<String, Object>> findAccountsByCustomerId(Long customerId) {

        try {

            List<Map<String, Object>> rows = db.query(
                    "SELECT * FROM accounts WHERE customer_id = ?",
                    customerId);

            return rows.isEmpty() || rows == null ? null : rows;

        } catch (SQLException e) {
            throw new DatabaseOperationException(
                    "Failed to retrieve accounts for customer id: " + customerId,
                    e);
        }
    }

    public List<Map<String, Object>> findAccountsByProductId(Long productId) {

        try {

            List<Map<String, Object>> rows = db.query(
                    "SELECT * FROM accounts WHERE product_id = ?",
                    productId);

            return rows.isEmpty() || rows == null ? null : rows;

        } catch (SQLException e) {

            throw new DatabaseOperationException(
                    "Failed to retrieve accounts for product id: " + productId,
                    e);
        }
    }

    public Long insert(Map<String, Object> accountFields) {

        try {
            List<Map<String, Object>> rows = db.query(
                    " INSERT INTO accounts(account_number, customer_id,product_id,balance,status,opening_date,is_locked) VALUES (?, ?, ?, ?, ?, ?, ?)",
                    accountFields.get("account_number"),
                    accountFields.get("customer_id"),
                    accountFields.get("product_id"),
                    accountFields.get("balance"),
                    accountFields.get("status"),
                    accountFields.get("opening_date"),
                    accountFields.get("is_locked"));

            return (Long) rows.get(0).get("id");

        } catch (SQLException e) {

            throw new DatabaseOperationException(
                    "Failed to create account",
                    e);
        }
    }

    public int update(Long id, Map<String, Object> changedFields) {

        try {
            List<Map<String, Object>> result = db.query(
                    "UPDATE accounts SET account_number = ?,customer_id = ?,product_id = ?, balance = ?,status = ?,opening_date = ?,is_locked = ? WHERE id = ?",
                    changedFields.get("account_number"),
                    changedFields.get("customer_id"),
                    changedFields.get("product_id"),
                    changedFields.get("balance"),
                    changedFields.get("status"),
                    changedFields.get("opening_date"),
                    changedFields.get("is_locked"),
                    id);

            return ((Number) result.get(0).get("affected_rows")).intValue();

        } catch (SQLException e) {

            throw new DatabaseOperationException(
                    "Failed to update account with id: " + id,
                    e);
        }
    }

    public int lockAccount(Long id) {

        try {

            List<Map<String, Object>> result = db
                    .query("UPDATE accounts SET is_locked = TRUE, status = 'ACTIVE' WHERE id = ?", id);

            return ((Number) result.get(0).get("affected_rows")).intValue();

        } catch (SQLException e) {

            throw new DatabaseOperationException(
                    "Failed to lock account with id: " + id,
                    e);
        }
    }

    public int unlockAccount(Long id) {

        try {

            List<Map<String, Object>> result = db
                    .query("UPDATE accounts SET is_locked=FALSE,status='ACTIVE' WHERE id=?", id);

            return ((Number) result.get(0).get("affected_rows")).intValue();

        } catch (SQLException e) {

            throw new DatabaseOperationException(
                    "Failed to unlock account with id: " + id,
                    e);
        }
    }

    public List<Map<String, Object>> findAll() {

        try {

            List<Map<String, Object>> rows = db.query(
                    "SELECT * FROM accounts");

            return rows.isEmpty() || rows == null ? null : rows;

        } catch (SQLException e) {

            throw new DatabaseOperationException(
                    "Failed to retrieve all accounts",
                    e);
        }
    }

    public List<Map<String, Object>> gteAccountWithProductByCustomerId(Long id) {
        try {
            List<Map<String, Object>> rows = db.query(
                    "SELECT * FROM accounts join products on accounts.product_id=products.id WHERE customer_id=?", id);
            return rows.isEmpty() || rows == null ? null : rows;

        } catch (SQLException e) {
            throw new DatabaseOperationException("failed to return data", e);
        }
    }
}