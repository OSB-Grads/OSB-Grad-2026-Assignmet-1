# Database Configuration

This project uses separate databases for the application and tests to ensure data isolation and prevent test data from interfering with application data.

## Database Files

- **Application Database**: `banking_app.db` - Used by the main application
- **Test Database**: `banking_app_test.db` - Used during testing (automatically created and cleaned up)
- **Real-World Bank Database**: `real_world_bank.db` - Owned by the separate
  real-world bank application (`real-world-bank/`). The only table in it this
  application may touch is its `inbox`.

All are SQLite files and are git-ignored. Run both applications from the
repository root so they resolve the same files.

## Connection settings

On connect, `DatabaseManager` enables two SQLite pragmas:

- `PRAGMA foreign_keys = ON` — SQLite ignores declared `FOREIGN KEY`s unless this
  is on. With it on, inserting a row with a bad `customer_id` actually fails.
- `PRAGMA busy_timeout = 5000` — wait up to 5s for a lock instead of failing
  immediately if another process holds the file.

## Running SQL

You write the SQL with `?` placeholders for values;
`query(String sql, Object... params)` runs it as a `PreparedStatement` and
returns `List<Map<String,Object>>` (one map per row for SELECT; a single result
with `affected_rows` / `generated_key` for INSERT/UPDATE/DELETE). Values are
bound after the SQL is parsed, so they can never be re-interpreted as SQL —
never concatenate values into the string.

For statements that must all succeed or all fail together (e.g. a transfer):

```java
try {
    db.startTransaction();
    db.query("UPDATE accounts SET balance = balance - ? WHERE id = ?", amount, fromId);
    db.query("UPDATE accounts SET balance = balance + ? WHERE id = ?", amount, toId);
    db.endTransaction();          // commit
} catch (SQLException e) {
    db.rollbackTransaction();      // undo
    throw e;
}
```

## Writing to the real-world bank's inbox

`DatabaseManager.connectTo(path)` opens a standalone (non-singleton) connection
to another application's database file without creating our tables in it:

```java
DatabaseManager rw = DatabaseManager.connectTo("real_world_bank.db");
try {
    rw.query("INSERT INTO inbox (...) VALUES (?, ?, ?, ?, ?)", /* values */);
} finally {
    rw.close();   // not the singleton — you opened it, you close it
}
```

A transaction cannot span the two files — see `DEVELOPMENT_GUIDE.md` for what
that means for the payment flow.

## How It Works

### DatabaseManager Class

The `DatabaseManager` class has been modified to support configurable database URLs:

- `getInstance()` - Returns the singleton instance using the main application database
- `getTestInstance()` - Returns a singleton instance configured for testing with the test database
- `resetInstance()` - Resets the singleton (useful for testing cleanup)

### Test Configuration

The test class `DatabaseManagerTest` uses the following setup:

1. **@BeforeEach**: Calls `DatabaseManager.getTestInstance()` to ensure tests use the test database
2. **@AfterEach**: Cleans up test data from the current test
3. **@AfterAll**: Resets the singleton instance and deletes the test database file

## Benefits

1. **Data Isolation**: Tests don't interfere with application data
2. **Clean Testing Environment**: Each test run starts with a fresh database
3. **No Manual Cleanup**: Test database is automatically created and destroyed
4. **Backward Compatibility**: Existing application code continues to work unchanged

## Usage

### For Application Code
```java
DatabaseManager dbManager = DatabaseManager.getInstance();
// Uses banking_app.db
```

### For Test Code
```java
DatabaseManager dbManager = DatabaseManager.getTestInstance();
// Uses banking_app_test.db
```

## Running Tests

```bash
mvn test
```

Tests will automatically:
1. Create a test database (`banking_app_test.db`)
2. Run all tests using the test database
3. Clean up and delete the test database after completion

## Running the Application

```bash
mvn compile exec:java
```

The application will use the main database (`banking_app.db`) and will not be affected by test data.
