package com.bank.db.customer;

import com.bank.db.DatabaseManager;

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
        // TODO: SELECT * FROM customers WHERE username = ... and return the first row (or null).
        throw new UnsupportedOperationException("TODO: implement findByUsername");
    }

    /**
     * Find a single customer by primary key.
     * @param id the customer id
     * @return the raw row, or {@code null} if not found
     */
    public Map<String, Object> findById(Long id) throws SQLException {
        // TODO: SELECT * FROM customers WHERE id = ...
        throw new UnsupportedOperationException("TODO: implement findById");
    }

    /**
     * Insert a new customer.
     * @return the generated customer id
     */
    public Long insert(Map<String, Object> customerFields) throws SQLException {
        // TODO: INSERT INTO customers (...) VALUES (...) and return the generated key.
        throw new UnsupportedOperationException("TODO: implement insert");
    }

    /**
     * Update an existing customer's mutable profile fields.
     * @return number of rows affected
     */
    public int update(Long id, Map<String, Object> changedFields) throws SQLException {
        // TODO: UPDATE customers SET ... WHERE id = ...
        throw new UnsupportedOperationException("TODO: implement update");
    }

    /**
     * List every customer (admin use).
     * @return raw rows
     */
    public List<Map<String, Object>> findAll() throws SQLException {
        // TODO: SELECT * FROM customers
        throw new UnsupportedOperationException("TODO: implement findAll");
    }
}
