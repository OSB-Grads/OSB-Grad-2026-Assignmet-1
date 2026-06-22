package com.bank.db.repository;

import com.bank.db.DatabaseManager;
import com.bank.dto.AccountDTO;
import com.bank.exception.DatabaseOperationException;
import com.bank.mapper.AccountMapper;
import com.bank.utils.UuidGeneratorUtil;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class AccountRepository {

    private final DatabaseManager db;

    public AccountRepository() {
        this.db = DatabaseManager.getInstance();
    }

    public Map<String, Object> findAccountById(String id) {

        try {

            List<Map<String, Object>> row = db.query(
                    "SELECT * FROM accounts WHERE id = ?",
                    id);

            return row.isEmpty() || row == null ? null : row.get(0);

        } catch (SQLException e) {

            throw new DatabaseOperationException("Failed to retrieve account", e);
        }
    }


    public AccountDTO findAccountByAccountNumber(String accountNumber){
        try {
            List<Map<String, Object>> row = db.query(
                    "SELECT * FROM accounts WHERE account_number = ?",
                    accountNumber);

            if(row.isEmpty()){
                return null;
            }

            Map<String,Object> account = row.get(0);
            AccountDTO accountDTO = AccountMapper.toDTO(account);
            return accountDTO;

        } catch (SQLException e) {

            throw new DatabaseOperationException("Failed to retrieve account", e);
        }
    }

    public List<Map<String, Object>> findAccountsByCustomerId(String customerId) {

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

    public List<Map<String, Object>> findAccountsByProductId(String productId) {

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

    public String insert(Map<String, Object> accountFields) {

        try {
            String accId=UuidGeneratorUtil.generateUuid();
            accountFields.put("id", accId);
            List<Map<String, Object>> rows = db.query(
                    " INSERT INTO accounts(id,account_number,customer_id,product_id,balance,status,is_locked) VALUES (?,?, ?, ?, ?, ?)",
                    accountFields.get("id"),
                    accountFields.get("account_number"),
                    accountFields.get("customer_id"),
                    accountFields.get("product_id"),
                    accountFields.get("balance"),
                    accountFields.get("status"),
                    accountFields.get("is_locked"));
                return ((String) rows.get(0).get("generated_key"));

        } catch (SQLException e) {

            throw new DatabaseOperationException(
                    "Failed to create account",
                    e);
        }
    }

    public int update(Long id, Map<String, Object> changedFields) {

        try {
            List<Map<String, Object>> result = db.query(
                    "UPDATE accounts SET customer_id = ?,product_id = ?, balance = ?,status = ?,is_locked = ? WHERE id = ?",
                    changedFields.get("customer_id"),
                    changedFields.get("product_id"),
                    changedFields.get("balance"),
                    changedFields.get("status"),
                    changedFields.get("is_locked"),
                    id);
            return ((Number) result.get(0).get("affected_rows")).intValue();

        } catch (SQLException e) {

            throw new DatabaseOperationException(
                    "Failed to update account with id: " + id,
                    e);
        }
    }

    public int lockAccount(String id) {

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

    public int unlockAccount(String id) {

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

    public List<Map<String, Object>> getAccountsWithProductByCustomerId(String id) {
        try {
            List<Map<String, Object>> rows = db.query(
                    "SELECT " +
                            "a.id, a.account_number,a.customer_id, a.product_id, " +
                            "a.balance, a.status, a.is_locked, " +
                            "p.product_name, p.category " +
                            "FROM accounts a " +
                            "JOIN products p " +
                            "ON a.product_id = p.id " +
                            "WHERE a.customer_id = ?",
                    id
            );

            return rows;

        } catch (SQLException e) {
            throw new DatabaseOperationException("failed to return data", e);
        }
    }
}