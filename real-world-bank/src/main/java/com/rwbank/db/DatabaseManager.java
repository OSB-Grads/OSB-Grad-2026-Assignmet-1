package com.rwbank.db;

import java.sql.*;
import java.util.*;

/**
 * Database layer for the real-world bank application.
 *
 * <p>Same shape as the banking application's DatabaseManager: you write the
 * SQL with {@code ?} placeholders, {@link #query(String, Object...)} runs it
 * as a PreparedStatement. This application owns
 * {@code real_world_bank.db}; its {@code inbox} table is the only table the
 * banking application is allowed to touch (and vice versa &mdash; use
 * {@link #connectTo(String)} to write results into the banking application's
 * inbox, nothing else).</p>
 */
public class DatabaseManager {
    private static DatabaseManager instance;
    private Connection connection;

    private static final String DEFAULT_DB_URL = "jdbc:sqlite:real_world_bank.db";
    private String dbUrl;
    private boolean ownsSchema;

    private DatabaseManager(String dbUrl, boolean ownsSchema) {
        this.dbUrl = dbUrl;
        this.ownsSchema = ownsSchema;
        initializeDatabase();
    }

    /**
     * Get singleton instance bound to real_world_bank.db
     * @return DatabaseManager instance
     */
    public static synchronized DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager(DEFAULT_DB_URL, true);
        }
        return instance;
    }

    /**
     * Open a connection to ANOTHER application's database file &mdash; the
     * banking application's &mdash; without creating this application's tables
     * in it. Use it ONLY to insert result messages into their {@code inbox}.
     * The returned instance is NOT the singleton &mdash; close it when done.
     *
     * @param dbFilePath path to the other application's SQLite file,
     *                   e.g. {@code "banking_app.db"}
     * @return a standalone DatabaseManager bound to that file
     */
    public static DatabaseManager connectTo(String dbFilePath) {
        return new DatabaseManager("jdbc:sqlite:" + dbFilePath, false);
    }

    /**
     * Initialize database connection and create tables if they don't exist
     */
    private void initializeDatabase() {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(dbUrl);

            // SQLite ignores declared FOREIGN KEYs unless switched on per-connection.
            // busy_timeout lets this process wait up to 5s for the file lock instead
            // of failing immediately when the banking application is writing.
            try (Statement pragma = connection.createStatement()) {
                pragma.execute("PRAGMA foreign_keys = ON");
                pragma.execute("PRAGMA busy_timeout = 5000");
            }

            if (ownsSchema) {
                createTables();
            }

            System.out.println("[rw-bank] Database initialized: " + dbUrl);

        } catch (ClassNotFoundException e) {
            System.err.println("[rw-bank] SQLite JDBC driver not found.");
        } catch (SQLException e) {
            System.err.println("[rw-bank] Database initialization failed: " + e.getMessage());
        }
    }

    /**
     * Create the real-world bank's tables.
     */
    private void createTables() {
        String[] createTableQueries = {
            // The world outside the bank: customers' money held elsewhere.
            // Reference data for KYC and source-of-wealth checks.
            "CREATE TABLE IF NOT EXISTS real_world_accounts (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "rw_account_number VARCHAR(20) UNIQUE NOT NULL, " +
            "sort_code VARCHAR(8) NOT NULL, " +
            "holder_name VARCHAR(100) NOT NULL, " +
            "national_id VARCHAR(20) NOT NULL, " +
            "balance DECIMAL(15,2) NOT NULL DEFAULT 0.00, " +
            "kyc_verified INTEGER NOT NULL DEFAULT 0, " +
            "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
            "updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
            ")",

            // Inbox — the integration contract with the banking application.
            // The banking application INSERTs request messages here
            // (DEBIT_REQUEST, CREDIT_REQUEST); this application's scheduler
            // processes them and writes *_RESULT messages into the banking
            // application's inbox. Identical schema on both sides.
            //
            // correlation_id  — matches a response to the request that caused it
            // idempotency_key — receiver-side dedupe; UNIQUE so a redelivered
            //                   message fails to insert instead of applying twice
            // transaction_id  — the business transaction (the bank's ledger identity)
            "CREATE TABLE IF NOT EXISTS inbox (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "correlation_id VARCHAR(64) NOT NULL, " +
            "idempotency_key VARCHAR(64) NOT NULL UNIQUE, " +
            "transaction_id VARCHAR(64), " +
            "message_type VARCHAR(40) NOT NULL, " +
            "payload TEXT NOT NULL, " +
            "status VARCHAR(20) NOT NULL DEFAULT 'PENDING' CHECK (status IN ('PENDING', 'PROCESSING', 'DONE', 'FAILED')), " +
            "reason TEXT, " +
            "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
            "processed_at TIMESTAMP" +
            ")"
        };

        for (String query : createTableQueries) {
            try {
                query(query);
            } catch (Exception e) {
                System.err.println("[rw-bank] Failed to create table: " + e.getMessage());
            }
        }
    }

    /**
     * Execute a SQL statement and return results as a list of maps.
     * Each map represents a row with column names as keys.
     *
     * <p>Write the SQL with {@code ?} placeholders and pass the values after
     * it — they are bound via PreparedStatement, so a value can never be
     * re-interpreted as SQL. Never concatenate values into the SQL string.</p>
     *
     * @param sql    The SQL to execute, with {@code ?} placeholders for values
     * @param params One value per {@code ?}, in order
     * @return List of rows, where each row is a Map&lt;String, Object&gt;.
     *         For INSERT/UPDATE/DELETE: a single result with {@code affected_rows}
     *         (and {@code generated_key} for INSERT).
     * @throws SQLException if execution fails
     */
    public List<Map<String, Object>> query(String sql, Object... params) throws SQLException {
        List<Map<String, Object>> results = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            for (int i = 0; i < params.length; i++) {
                statement.setObject(i + 1, params[i]);
            }

            if (sql.trim().toUpperCase().startsWith("SELECT")) {
                try (ResultSet resultSet = statement.executeQuery()) {
                    ResultSetMetaData metaData = resultSet.getMetaData();
                    int columnCount = metaData.getColumnCount();

                    while (resultSet.next()) {
                        Map<String, Object> row = new HashMap<>();
                        for (int i = 1; i <= columnCount; i++) {
                            row.put(metaData.getColumnName(i), resultSet.getObject(i));
                        }
                        results.add(row);
                    }
                }
            } else {
                int affectedRows = statement.executeUpdate();
                Map<String, Object> result = new HashMap<>();
                result.put("affected_rows", affectedRows);

                if (sql.trim().toUpperCase().startsWith("INSERT")) {
                    try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            result.put("generated_key", generatedKeys.getLong(1));
                        }
                    }
                }

                results.add(result);
            }
        }

        return results;
    }

    /**
     * Start a transaction by turning off auto-commit. Every query after this
     * runs in the same transaction until {@link #endTransaction()} (commit)
     * or {@link #rollbackTransaction()} (undo).
     * @throws SQLException if auto-commit cannot be changed
     */
    public void startTransaction() throws SQLException {
        connection.setAutoCommit(false);
    }

    /**
     * Commit everything since {@link #startTransaction()}, then return to
     * auto-commit mode.
     * @throws SQLException if the commit fails
     */
    public void endTransaction() throws SQLException {
        try {
            connection.commit();
        } finally {
            connection.setAutoCommit(true);
        }
    }

    /**
     * Undo everything since {@link #startTransaction()}, then return to
     * auto-commit mode. Call from a catch block on partial failure.
     * @throws SQLException if the rollback fails
     */
    public void rollbackTransaction() throws SQLException {
        try {
            connection.rollback();
        } finally {
            connection.setAutoCommit(true);
        }
    }

    /**
     * Close database connection
     */
    public void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            System.err.println("[rw-bank] Error closing connection: " + e.getMessage());
        }
    }
}
