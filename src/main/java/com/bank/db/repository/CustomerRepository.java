package com.bank.db.repository;

import com.bank.db.DatabaseManager;
import com.bank.exception.DatabaseOperationException;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Data access (DAO) for customers. Repositories stay <strong>small</strong>:
 * only CRUD and simple lookups. No business rules here &mdash; those belong in
 * the service / orchestrator layer.
 *
 * <p>Repositories return raw database rows ({@code Map<String,Object>}); the
 * {@link com.bank.mapper.CustomerMapper} turns those rows into DTOs. This keeps
 * the database shape out of the higher layers.</p>
 *
 * <p>This is a skeleton showing the expected shape &mdash; method bodies are
 * intentionally unimplemented.</p>
 */
public class CustomerRepository {

    private final DatabaseManager db;

    public CustomerRepository() {
        this.db = DatabaseManager.getInstance();
    }

    /**
     * Find a single customer by username.
     * @param username the login username
     * @return the raw row, or {@code null} if no such customer exists
     */
    public Map<String, Object> findByUsername(String username) throws SQLException {
            List<Map<String, Object>> results =
                    db.query(
                            "SELECT * FROM customers WHERE username = ?"
                            ,username
                    );
            if (results.isEmpty()) {
                return null;
            }
            return results.get(0);
    }

    /**
     * Find a single customer by primary key.
     * @param id the customer id
     * @return the raw row, or {@code null} if not found
     */
    public Map<String, Object> findById(String  id) throws SQLException {
        try {
            List<Map<String, Object>> results =
                    db.query(
                            "SELECT * FROM customers where id = ?"
                            ,id
                    );
            if (results.isEmpty()) {
                return null;
            }
            return results.get(0);
        }catch(SQLException e) {
            throw new DatabaseOperationException("Failed to retrieve Customer with customer ID: " + id,e);
        }
    }

    /**
     * Insert a new customer.
     * @return the generated customer id
     */
    public String insert(Map<String, Object> customerFields) throws SQLException {
        try {
            List<Map<String, Object>> results =
                    db.query(
                            "INSERT INTO customers " +
                                    "(id ,first_name, last_name, " +
                                    "date_of_birth, email, phone, address, national_id) " +
                                    "VALUES (? ,?, ?, ?, ?, ?, ?, ?)",

                            customerFields.get("id"),
                            customerFields.get("first_name"),
                            customerFields.get("last_name"),
                            customerFields.get("date_of_birth"),
                            customerFields.get("email"),
                            customerFields.get("phone"),
                            customerFields.get("address"),
                            customerFields.get("national_id")
                    );

            if (results.isEmpty()) {
                return null;
            }

            return (String) results.get(0).get("id");

        } catch (SQLException e) {
            throw new DatabaseOperationException(
                    "Failed to insert customer",
                    e
            );
        }
    }

    /**
     * Update an existing customer's mutable profile fields.
     * @return number of rows affected
     */
    public int update(String id, Map<String, Object> changedFields) throws SQLException {
        try {
            List<Map<String, Object>> results =
                    db.query(
                            "UPDATE customers SET " +
                                    "first_name = ?, " +
                                    "last_name = ?, " +
                                    "date_of_birth = ?, " +
                                    "email = ?, " +
                                    "phone = ?, " +
                                    "address = ?, " +
                                    "national_id = ?, " +
                                    "updated_at = CURRENT_TIMESTAMP " +
                                    "WHERE id = ?",

                            changedFields.get("first_name"),
                            changedFields.get("last_name"),
                            changedFields.get("date_of_birth"),
                            changedFields.get("email"),
                            changedFields.get("phone"),
                            changedFields.get("address"),
                            changedFields.get("national_id"),
                            id
                    );
            if (results.isEmpty()) {
                return 0;
            }
            return ((Number) results.get(0)
                    .get("affected_rows"))
                    .intValue();
        }catch(SQLException e) {
            throw new DatabaseOperationException("Failed to update in database", e);
        }
    }

    /**
     * List every customer (admin use).
     * @return raw rows
     */
    public List<Map<String, Object>> findAll() throws SQLException {
        try {
            return db.query( "SELECT * FROM customers");
        } catch (SQLException e) {
            throw new DatabaseOperationException("Failed to fetch all the customers", e);
        }
    }
}
