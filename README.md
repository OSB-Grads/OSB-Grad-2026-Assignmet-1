# Java CLI Banking Application — Assignment Bootstrap

A starting skeleton for the CLI Banking Application assignment (a specialist
savings & lending bank). It gives you a working build, a database layer, sample
DTOs/exceptions, an example vertical slice, and the menu shell. You implement
the services, mappers, orchestrators and the rest of the business logic.

## Two applications

This repository contains **two separate applications**:

1. **The bank** (this project, root) — the CLI banking application you build.
2. **The real-world bank** (`real-world-bank/`) — a standalone app simulating
   the world outside the bank: customers' money held elsewhere. Own database,
   own scheduler, own build. See [real-world-bank/README.md](real-world-bank/README.md).

They never call each other. Integration is asynchronous: each application owns
an `inbox` table that the *other* application inserts messages into, and each
application's scheduler processes only its own inbox. (This is how real
interbank payments work — batch queues, not synchronous APIs.)

## Running

Run everything from the **repository root** so both applications find the same
`.db` files.

```bash
# The bank
mvn compile exec:java                          # compile and run
mvn package                                    # or build a runnable JAR
java -jar target/cli-banking-app-1.0.0.jar

# The real-world bank (build once, seed once)
mvn -f real-world-bank/pom.xml package
java -jar real-world-bank/target/real-world-bank-1.0.0.jar seed

# Schedulers — run by cron / Windows Task Scheduler, or by hand
java -cp target/cli-banking-app-1.0.0.jar com.bank.scheduler.SchedulerMain payment-processor
java -cp real-world-bank/target/real-world-bank-1.0.0.jar com.rwbank.scheduler.SchedulerMain rw-inbox-processor
```

Requires Java 11+ and Maven 3.6+.

## Package structure

Matches the recommended structure in the assignment:

```
src/main/java/com/bank/
├── cli/                 # Main entry point
│   └── display/         # CLI menus, input, printing (keep thin)
├── auth/                # AuthService, password hashing, authorisation
├── customer/            # CustomerService — profile management
├── product/             # ProductService — categories & products
├── account/             # AccountService — open/view accounts
├── payment/             # PaymentService + processor (deposit/withdraw queue)
├── realbank/            # RealBankService — sends requests into the real-world bank's inbox
├── transfer/            # TransferService — transfer rules
├── loan/                # LoanService — assessment, offer, repayment
├── logging/             # LogService
├── scheduler/           # ScheduledJob, JobResult, SchedulerMain (provided) — external scheduler entry point
├── db/                  # DatabaseManager (provided)
│   └── customer/        # CustomerRepository (example) — one sub-package per module
├── dto/                 # Data Transfer Objects
├── mapper/              # Entity/row ↔ DTO mappers
├── orchestrator/        # Coordinators for multi-service workflows
└── exception/           # Custom exceptions
```

The real-world bank application lives in `real-world-bank/` with its own
`pom.xml` and packages (`com.rwbank.*`). You write code there too: its KYC
checks and inbox processor are skeletons.

> The `customer` slice (`db/customer/CustomerRepository`, `mapper/CustomerMapper`,
> `customer/CustomerService`, `orchestrator/AccountOpeningOrchestrator`) is a
> **skeleton with no logic** — it shows the layering to copy for every other
> module. Fill in the bodies; build the other modules the same way.

## What's provided

- **`com.bank.db.DatabaseManager`** — the database layer, including the `inbox`
  table and `connectTo()` for writing into the real-world bank's inbox. See below.
- **`com.bank.scheduler`** — `ScheduledJob`, `JobResult`, `SchedulerMain`. The
  contract for jobs an external scheduler (cron / Task Scheduler / a person)
  runs. You implement and register the jobs.
- **`com.bank.dto`** — `CustomerDTO`, `AccountDTO` examples (match the spec fields).
- **`com.bank.exception`** — `InvalidCredentialsException`, `InsufficientFundsException`
  examples. Add the rest (see the assignment's custom-exception list).
- **CLI shell** — `Main`, `MenuDisplay` with the unauthenticated / customer /
  admin menus wired to `TODO` handlers.
- **Example vertical slice** — the `customer` module skeleton.
- **The real-world bank app** (`real-world-bank/`) — its database layer, seeded
  reference accounts (deliberately including failure cases), and scheduler
  harness. Its KYC and inbox-processing logic are skeletons you implement.

## Database layer

`DatabaseManager` is a singleton. You write the SQL — with `?` placeholders for
values — and it runs as a `PreparedStatement`.

```java
DatabaseManager db = DatabaseManager.getInstance();

// SELECT -> List<Map<String,Object>> (one map per row, column name -> value)
List<Map<String,Object>> rows = db.query("SELECT * FROM customers WHERE id = ?", customerId);

// INSERT/UPDATE/DELETE -> single-row result with affected_rows (and generated_key for INSERT)
List<Map<String,Object>> result = db.query(
    "INSERT INTO customers (username, email) VALUES (?, ?)", username, email);
Long newId = (Long) result.get(0).get("generated_key");

// no values? omit the varargs
List<Map<String,Object>> all = db.query("SELECT * FROM products");
```

**Transactions** — when several statements must all succeed or all fail (e.g. a
transfer's debit + credit), wrap them:

```java
try {
    db.startTransaction();
    db.query("UPDATE accounts SET balance = balance - ? WHERE id = ?", amount, fromId);
    db.query("UPDATE accounts SET balance = balance + ? WHERE id = ?", amount, toId);
    db.endTransaction();            // commit
} catch (SQLException e) {
    db.rollbackTransaction();        // undo on failure
    throw e;
}
```

**Money** is `DECIMAL(15,2)` in the DB and `BigDecimal` in code — never `double`
(`0.1 + 0.2 != 0.3` in floating point; over many transactions balances drift).
Read money columns into `BigDecimal` in your mappers.

**Why the `?` placeholders matter** — the SQL is parsed *first*, the values are
bound *after*, so a value can never be re-interpreted as SQL. Concatenating user
input into the SQL string (`"... WHERE username = '" + input + "'"`) is SQL
injection: input like `x'; DROP TABLE customers;--` becomes part of the query.
Nothing physically stops you concatenating — `query()` runs whatever string you
pass — so the rule is yours to keep: **values go through `?`, always.**

## The schema

`DatabaseManager.createTables()` currently seeds a minimal schema (`users`,
`accounts`, `transactions`, `logs`, plus the provided `inbox`) — the first four
are left over from the bootstrap. The assignment needs more — you will extend
it. At minimum you'll add/adjust tables for:

- **customers** with role (CUSTOMER/ADMIN), first/last name, DOB, email (unique),
  phone, address, national ID
- **product_categories** and **products** (3-level hierarchy: category → product → account)
- **accounts** linked to a product, with status and opening date
- **payment_queue** (pending deposits/withdrawals the processor consumes)
- **loans** (+ loan categories / repayment schedule)
- a **transaction/ledger** record for every money movement

`real_world_accounts` is NOT in this database — it belongs to the real-world
bank application. The `inbox` table (provided, both sides) is the only shared
contract between the two applications.

## Talking to the real-world bank

Money in/out of the bank flows through asynchronous messages — see
[real-world-bank/README.md](real-world-bank/README.md) for the message flow and
the meaning of `correlation_id` / `idempotency_key` / `transaction_id`. To send
a request:

```java
DatabaseManager rw = DatabaseManager.connectTo("real_world_bank.db");
try {
    rw.query("INSERT INTO inbox (correlation_id, idempotency_key, transaction_id, message_type, payload) "
           + "VALUES (?, ?, ?, ?, ?)",
           correlationId, idempotencyKey, transactionId, "DEBIT_REQUEST",
           "rw_account_number=RW-1001;amount=250.00;national_id=QQ123456A");
} finally {
    rw.close();
}
```

Touch **only** their `inbox` — the rest of their database is private (by
convention; SQLite has no permissions, so the discipline is yours).

## Where to start

1. Read the assignment brief and `DEVELOPMENT_GUIDE.md`.
2. Extend the schema in `DatabaseManager.createTables()`.
3. Build one module end-to-end using the `customer` slice as the template:
   Repository (SQL) → Mapper (row ↔ DTO) → Service (rules) → Orchestrator (multi-service flow).
4. Wire the `MenuDisplay` `TODO` handlers to your services.

Good luck.
