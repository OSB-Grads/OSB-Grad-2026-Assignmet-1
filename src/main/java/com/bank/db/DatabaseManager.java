package com.bank.db;

import java.sql.*;
import java.util.*;

/**
 * Simple database layer that provides a single query function.
 * This class handles all database connections and operations.
 *
 * Usage: DatabaseManager.getInstance().query("SELECT * FROM auth WHERE username = ?", username)
 */
public class DatabaseManager {
    private static DatabaseManager instance;
    private Connection connection;
    
    // Database configuration
    private static final String DEFAULT_DB_URL = "jdbc:sqlite:banking_app.db";
    private static final String TEST_DB_URL = "jdbc:sqlite:banking_app_test.db";
    private String dbUrl;
    private boolean ownsSchema;

    private DatabaseManager() {
        this(DEFAULT_DB_URL, true);
    }

    private DatabaseManager(String dbUrl) {
        this(dbUrl, true);
    }

    private DatabaseManager(String dbUrl, boolean ownsSchema) {
        this.dbUrl = dbUrl;
        this.ownsSchema = ownsSchema;
        initializeDatabase();
    }
    
    /**
     * Get singleton instance of DatabaseManager
     * @return DatabaseManager instance
     */
    public static synchronized DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }
    
    /**
     * Get test instance of DatabaseManager with test database
     * @return DatabaseManager instance configured for testing
     */
    public static synchronized DatabaseManager getTestInstance() {
        if (instance != null) {
            instance.close();
        }
        instance = new DatabaseManager(TEST_DB_URL);
        return instance;
    }
    
    /**
     * Open a connection to ANOTHER application's database file &mdash; e.g. the
     * real-world bank's &mdash; without creating this application's tables in it.
     *
     * <p>Use this to insert messages into the other application's {@code inbox}
     * table. That inbox is the integration contract: do not read or write any
     * of the other application's private tables.</p>
     *
     * <p>The returned instance is NOT the singleton &mdash; close it when done.</p>
     *
     * <pre>{@code
     * DatabaseManager rw = DatabaseManager.connectTo("real_world_bank.db");
     * try {
     *     rw.query("INSERT INTO inbox (...) VALUES (?, ?, ?, ?, ?)", ...);
     * } finally {
     *     rw.close();
     * }
     * }</pre>
     *
     * @param dbFilePath path to the other application's SQLite file
     * @return a standalone DatabaseManager bound to that file
     */
    public static DatabaseManager connectTo(String dbFilePath) {
        return new DatabaseManager("jdbc:sqlite:" + dbFilePath, false);
    }

    /**
     * Reset the singleton instance (useful for testing)
     */
    public static synchronized void resetInstance() {
        if (instance != null) {
            instance.close();
            instance = null;
        }
    }
    
    /**
     * Initialize database connection and create tables if they don't exist
     */
    private void initializeDatabase() {
        try {
            // Load SQLite JDBC driver
            Class.forName("org.sqlite.JDBC");
            
            // Create connection
            connection = DriverManager.getConnection(dbUrl);

            // SQLite ignores declared FOREIGN KEYs unless this is switched on per-connection.
            // busy_timeout lets a second process (e.g. the external payment processor) wait
            // instead of failing immediately with SQLITE_BUSY when the file is write-locked.
            try (Statement pragma = connection.createStatement()) {
                pragma.execute("PRAGMA foreign_keys = ON");
                pragma.execute("PRAGMA busy_timeout = 5000");
            }

            // Create tables if they don't exist (skipped when connecting to
            // another application's database via connectTo)
            if (ownsSchema) {
                createTables();
            }

            System.out.println("Database initialized successfully.");
            
        } catch (ClassNotFoundException e) {
            System.err.println("SQLite JDBC driver not found. Please add sqlite-jdbc dependency to your project.");
            System.err.println("Add this to your pom.xml dependencies:");
            System.err.println("<dependency>");
            System.err.println("    <groupId>org.xerial</groupId>");
            System.err.println("    <artifactId>sqlite-jdbc</artifactId>");
            System.err.println("    <version>3.42.0.0</version>");
            System.err.println("</dependency>");
        } catch (SQLException e) {
            System.err.println("Database initialization failed: " + e.getMessage());
        }
    }
    
    /**
     * Create necessary tables for the banking application
     */
    private void createTables() {
        String[] createTableQueries = {
            // Customers table — profile data only, no credentials
            "CREATE TABLE IF NOT EXISTS customers (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "first_name VARCHAR(100) NOT NULL, " +
            "last_name VARCHAR(100) NOT NULL, " +
            "date_of_birth DATE NOT NULL,"+
            "email VARCHAR(100), " +
            "phone VARCHAR(20), " +
            "address VARCHAR(255) NOT NULL,"+
            "national_id VARCHAR(50) UNIQUE NOT NULL ,"+
            "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
            "updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
            ")",

            // Auth table — login credentials and authorisation role.
            // One auth record per customer (customer_id UNIQUE).
            "CREATE TABLE IF NOT EXISTS auth (" +
            "id VARCHAR(36) PRIMARY KEY, " +
            "username VARCHAR(50) UNIQUE NOT NULL, " +
            "password_hash VARCHAR(255) NOT NULL, " +
            //"customer_id INTEGER NOT NULL UNIQUE, " +
            "role VARCHAR(20) NOT NULL DEFAULT 'CUSTOMER' CHECK (role IN ('CUSTOMER', 'ADMIN')) " +
            //"FOREIGN KEY (customer_id) REFERENCES customers(id)" +
            ")",
            
            // Accounts table
            "CREATE TABLE IF NOT EXISTS accounts (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "customer_id INTEGER NOT NULL, " +
            "product_id INTEGER NOT NULL,"+
            "balance DECIMAL(15,2) DEFAULT 0.00, " +
            "is_locked BOOLEAN DEFAULT FALSE, " +
            "status VARCHAR(20) NOT NULL  DEFAULT 'ACTIVE' CHECK(status IN('ACTIVE','CLOSED','MATURED'))," +
            "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
            "updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
            "FOREIGN KEY (customer_id) REFERENCES customers(id)" +
            ")",
            
            // Transactions table
            "CREATE TABLE IF NOT EXISTS transactions (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, "
            +"customer_id INTEGER NOT NULL, " +
            "from_account_id INTEGER, " +
            "to_account_id INTEGER, " +
            "transaction_type VARCHAR(20) NOT NULL CHECK (transaction_type IN ('DEPOSIT', 'WITHDRAWAL', 'TRANSFER')), " +
            "amount DECIMAL(15,2) NOT NULL, " +
            "description TEXT, " +
            "status VARCHAR(20) DEFAULT 'COMPLETED' CHECK (status IN ('PENDING', 'COMPLETED', 'FAILED')), " +
            "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
            "updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
            "FOREIGN KEY (from_account_id) REFERENCES accounts(id), " +
            "FOREIGN KEY (to_account_id) REFERENCES accounts(id)" +
            "FOREIGN KEY (customer_id) REFERENCES customers(id)" +
            ")",

            // Logs table — customer_id is the acting customer (nullable: e.g.
            // failed logins where no customer was identified)
            "CREATE TABLE IF NOT EXISTS logs (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "customer_id INTEGER, " +
            "action VARCHAR(100) NOT NULL, " +
            "details TEXT, " +
            "ip_address VARCHAR(45), " +
            "status VARCHAR(20) DEFAULT 'SUCCESS' CHECK (status IN ('SUCCESS', 'FAILURE', 'ERROR')), " +
            "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
            "FOREIGN KEY (customer_id) REFERENCES customers(id)" +
            ")",

            // Inbox table — the integration contract with the real-world bank
            // application. The real-world bank's scheduler INSERTs result
            // messages (DEBIT_RESULT, CREDIT_RESULT) here; our schedulers read
            // and process them. Identical table exists in the real-world
            // bank's database for messages flowing the other way.
            //
            // correlation_id  — matches a response to the request that caused it
            // idempotency_key — receiver-side dedupe; UNIQUE so a redelivered
            //                   message fails to insert instead of applying twice
            // transaction_id  — the business transaction (ledger identity)
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
            ")",

             "CREATE TABLE IF NOT EXISTS products (" +
             "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
             "product_name VARCHAR(64) NOT NULL UNIQUE, " +
             "category VARCHAR(64) NOT NULL CHECK (category IN ('Savings', 'Limited Access','Fixed Deposits')), " +
             "interest_rate DECIMAL(4,2) NOT NULL, " +
             "min_operating_balance DECIMAL(15,2) NOT NULL , " +
             "term_months INTEGER " +
             ")"
        };
        
        for (String query : createTableQueries) {
            try {
                query(query);
            } catch (Exception e) {
                System.err.println("Failed to create table: " + e.getMessage());
            }
        }
    }
    
    /**
     * Execute a SQL statement and return results as a list of maps.
     * Each map represents a row with column names as keys.
     *
     * <p>You still write the SQL — but put a {@code ?} wherever a value goes
     * and pass the values after it. The statement runs as a
     * {@link PreparedStatement}: the database parses the SQL <em>first</em> and
     * binds the values <em>after</em>, so a value can never be re-interpreted
     * as SQL. <b>Never</b> build SQL by concatenating user input.</p>
     *
     * <pre>{@code
     * // SELECT
     * db.query("SELECT * FROM customers WHERE username = ?", username);
     *
     * // INSERT — generated key comes back
     * db.query("INSERT INTO customers (username, email) VALUES (?, ?)", username, email);
     *
     * // no values? just omit the varargs
     * db.query("SELECT * FROM products");
     * }</pre>
     *
     * @param sql    The SQL to execute, with {@code ?} placeholders for values
     * @param params One value per {@code ?}, in order
     * @return List of rows, where each row is a Map&lt;String, Object&gt;.
     *         For INSERT/UPDATE/DELETE: a single result with {@code affected_rows}
     *         (and {@code generated_key} for INSERT).
     * @throws SQLException if execution fails (including placeholder/value count mismatch)
     */
    public List<Map<String, Object>> query(String sql, Object... params) throws SQLException {
        List<Map<String, Object>> results = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            // Bind values to the ? placeholders, in order (JDBC is 1-based)
            for (int i = 0; i < params.length; i++) {
                statement.setObject(i + 1, params[i]);
            }

            // Check if it's a SELECT query (returns ResultSet)
            if (sql.trim().toUpperCase().startsWith("SELECT")) {
                try (ResultSet resultSet = statement.executeQuery()) {
                    ResultSetMetaData metaData = resultSet.getMetaData();
                    int columnCount = metaData.getColumnCount();

                    while (resultSet.next()) {
                        Map<String, Object> row = new HashMap<>();
                        for (int i = 1; i <= columnCount; i++) {
                            String columnName = metaData.getColumnName(i);
                            Object value = resultSet.getObject(i);
                            row.put(columnName, value);
                        }
                        results.add(row);
                    }
                }
            } else {
                // For INSERT, UPDATE, DELETE queries
                int affectedRows = statement.executeUpdate();
                Map<String, Object> result = new HashMap<>();
                result.put("affected_rows", affectedRows);

                // For INSERT queries, try to get the generated key
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
     * Start a transaction by turning off auto-commit.
     *
     * <p>Use this when several SQL statements must all succeed or all fail
     * together &mdash; for example debiting one account and crediting another
     * during a transfer. Without a transaction, a failure after the debit but
     * before the credit would lose money. Every {@link #query(String, Object...)}
     * call made after this runs as part of the same transaction until you call
     * {@link #endTransaction()} (commit) or {@link #rollbackTransaction()} (undo).</p>
     *
     * <pre>{@code
     * try {
     *     db.startTransaction();
     *     db.query("UPDATE accounts SET balance = balance - ? WHERE id = ?", amount, fromId);
     *     db.query("UPDATE accounts SET balance = balance + ? WHERE id = ?", amount, toId);
     *     db.endTransaction();          // commit: both updates are saved together
     * } catch (SQLException e) {
     *     db.rollbackTransaction();      // undo the debit if the credit failed
     *     throw e;
     * }
     * }</pre>
     *
     * @throws SQLException if auto-commit cannot be changed
     */
    public void startTransaction() throws SQLException {
        connection.setAutoCommit(false);
    }

    /**
     * End the current transaction by committing every change made since
     * {@link #startTransaction()}, then return to auto-commit mode.
     * @throws SQLException if the commit fails
     */
    public void endTransaction() throws SQLException {

        if (!connection.getAutoCommit()) {
            try {
                connection.commit();
            } finally {
                connection.setAutoCommit(true);
            }
        }
    }
    /**
     * Abort the current transaction &mdash; undoing every change made since
     * {@link #startTransaction()} &mdash; then return to auto-commit mode.
     * Call this from a catch block when something fails part-way through.
     * @throws SQLException if the rollback fails
     */
    public void rollbackTransaction() throws SQLException {

            if (!connection.getAutoCommit()) {
                try {
                    connection.rollback();
                } finally {
                    connection.setAutoCommit(true);
                }
            }

        }

    /**
     * Close database connection
     */
    public void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Database connection closed.");
            }
        } catch (SQLException e) {
            System.err.println("Error closing database connection: " + e.getMessage());
        }
    }
    
    /**
     * Check if database connection is valid
     * @return true if connection is valid, false otherwise
     */
    public boolean isConnected() {
        try {
            return connection != null && !connection.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }
}
