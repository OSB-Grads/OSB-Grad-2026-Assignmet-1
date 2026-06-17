package com.bank.db;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Sample test class to demonstrate how to test the DatabaseManager.
 * This shows freshers how to write unit tests for their application.
 */
public class DatabaseManagerTest {
    
    private DatabaseManager dbManager;
    
    @BeforeEach
    public void setUp() {
        // Use test database instance instead of regular instance
        dbManager = DatabaseManager.getTestInstance();
    }
    
    @AfterEach
    public void tearDown() {
        // Clean up test data if needed
        try {
            dbManager.query("DELETE FROM transactions WHERE transaction_id LIKE 'TEST_%'");
            dbManager.query("DELETE FROM accounts WHERE account_number LIKE 'ACC%'");
            dbManager.query("DELETE FROM logs WHERE action = 'TEST_ACTION'");
            dbManager.query("DELETE FROM auth WHERE username LIKE 'test_%'");
            dbManager.query("DELETE FROM customers WHERE full_name LIKE 'Test %' OR full_name LIKE '% Test %'");
        } catch (SQLException e) {
            // Ignore cleanup errors in tests
        }
    }
    
    @AfterAll
    public static void cleanupTestDatabase() {
        // Reset the singleton instance to ensure clean state
        DatabaseManager.resetInstance();
        
        // Optionally delete the test database file
        try {
            java.io.File testDbFile = new java.io.File("banking_app_test.db");
            if (testDbFile.exists()) {
                testDbFile.delete();
                System.out.println("Test database file deleted successfully.");
            }
        } catch (Exception e) {
            System.err.println("Could not delete test database file: " + e.getMessage());
        }
    }
    
    @Test
    public void testDatabaseConnection() {
        assertTrue(dbManager.isConnected(), "Database should be connected");
    }
    
    /**
     * Insert a customer profile row and a linked auth row,
     * returning the generated customer id.
     */
    private Long createTestCustomer(String username, String fullName, String email, String phone) throws SQLException {
        String customerSql = "INSERT INTO customers (first_name, email, phone) " +
                            "VALUES ('" + fullName + "', '" + email + "', '" + phone + "')";
        List<Map<String, Object>> customerResult = dbManager.query(customerSql);
        Long customerId = (Long) customerResult.get(0).get("generated_key");

        String authSql = "INSERT INTO auth (username, password_hash, customer_id, role) " +
                        "VALUES ('" + username + "', 'hashed_password', " + customerId + ", 'CUSTOMER')";
        dbManager.query(authSql);

        return customerId;
    }

    @Test
    public void testCreateAndRetrieveCustomer() throws SQLException {
        Long customerId = createTestCustomer("test_user", "Test User", "test@example.com", "1234567890");
        assertNotNull(customerId, "Should get generated customer ID");

        // Retrieve the customer profile
        String selectSql = "SELECT * FROM customers WHERE id = " + customerId;
        List<Map<String, Object>> selectResult = dbManager.query(selectSql);

        assertFalse(selectResult.isEmpty(), "Should find the inserted customer");
        Map<String, Object> customer = selectResult.get(0);
        assertEquals("Test User", customer.get("full_name"));
        assertEquals("test@example.com", customer.get("email"));

        // Retrieve the linked auth record
        String authSql = "SELECT * FROM auth WHERE customer_id = " + customerId;
        List<Map<String, Object>> authResult = dbManager.query(authSql);

        assertFalse(authResult.isEmpty(), "Should find the linked auth record");
        Map<String, Object> auth = authResult.get(0);
        assertEquals("test_user", auth.get("username"));
        assertEquals("CUSTOMER", auth.get("role"));
    }
    
    @Test
    public void testCreateAccount() throws SQLException {
        // First create a customer
        Long customerId = createTestCustomer("test_account_user", "Account Test User", "account@example.com", "9876543210");

        // Create an account for the customer
        String accountSql = "INSERT INTO accounts (account_number, customer_id, account_type, balance) " +
                           "VALUES ('ACC001', " + customerId + ", 'SAVINGS', 1000.00)";
        
        List<Map<String, Object>> accountResult = dbManager.query(accountSql);
        assertFalse(accountResult.isEmpty(), "Account creation should return result");
        
        Long accountId = (Long) accountResult.get(0).get("generated_key");
        assertNotNull(accountId, "Should get generated account ID");
        
        // Verify the account
        String selectSql = "SELECT * FROM accounts WHERE id = " + accountId;
        List<Map<String, Object>> selectResult = dbManager.query(selectSql);
        
        assertFalse(selectResult.isEmpty(), "Should find the created account");
        Map<String, Object> account = selectResult.get(0);
        assertEquals("ACC001", account.get("account_number"));
        assertEquals("SAVINGS", account.get("account_type"));
        assertEquals(1000.0, ((Number) account.get("balance")).doubleValue(), 0.01);
    }
    
    @Test
    public void testUpdateAccountBalance() throws SQLException {
        // Create customer and account first
        Long customerId = createTestCustomer("test_balance_user", "Balance Test User", "balance@example.com", "5555555555");

        String accountSql = "INSERT INTO accounts (account_number, customer_id, account_type, balance) " +
                           "VALUES ('ACC002', " + customerId + ", 'SAVINGS', 500.00)";
        
        List<Map<String, Object>> accountResult = dbManager.query(accountSql);
        Long accountId = (Long) accountResult.get(0).get("generated_key");
        
        // Update the balance
        String updateSql = "UPDATE accounts SET balance = 750.00 WHERE id = " + accountId;
        List<Map<String, Object>> updateResult = dbManager.query(updateSql);
        
        assertEquals(1, ((Number) updateResult.get(0).get("affected_rows")).intValue(), 
                    "Should update exactly one row");
        
        // Verify the update
        String selectSql = "SELECT balance FROM accounts WHERE id = " + accountId;
        List<Map<String, Object>> selectResult = dbManager.query(selectSql);
        
        assertEquals(750.0, ((Number) selectResult.get(0).get("balance")).doubleValue(), 0.01,
                    "Balance should be updated to 750.00");
    }
    
    @Test
    public void testTransactionLogging() throws SQLException {
        // Create a customer to act as the logged-in principal
        Long customerId = createTestCustomer("test_log_user", "Log Test User", "log@example.com", "1112223333");

        // Test logging functionality
        String logSql = "INSERT INTO logs (customer_id, action, details, status) " +
                       "VALUES (" + customerId + ", 'TEST_ACTION', 'This is a test log entry', 'SUCCESS')";
        
        List<Map<String, Object>> logResult = dbManager.query(logSql);
        assertFalse(logResult.isEmpty(), "Log insertion should return result");
        
        Long logId = (Long) logResult.get(0).get("generated_key");
        assertNotNull(logId, "Should get generated log ID");
        
        // Verify the log entry
        String selectSql = "SELECT * FROM logs WHERE id = " + logId;
        List<Map<String, Object>> selectResult = dbManager.query(selectSql);
        
        assertFalse(selectResult.isEmpty(), "Should find the log entry");
        Map<String, Object> log = selectResult.get(0);
        assertEquals("TEST_ACTION", log.get("action"));
        assertEquals("This is a test log entry", log.get("details"));
        assertEquals("SUCCESS", log.get("status"));
    }
}
