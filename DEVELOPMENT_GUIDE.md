# Development Guide — CLI Banking Application

A suggested path through the assignment. Build one module end-to-end, test it,
then move on. Use the provided `customer` slice as the template for layering.

## Prerequisites

- Java 11+
- Maven 3.6+
- An IDE (IntelliJ IDEA, Eclipse, VS Code)

```bash
mvn compile exec:java        # run
mvn package                  # build JAR
mvn test                     # run tests
```

## The layering (copy this for every module)

```
CLI (MenuDisplay)            thin — read input, call a service/orchestrator, print result
  -> Orchestrator            only for flows spanning multiple services; owns the transaction
  -> Service                 business rules; validates; returns DTOs (never rows/entities)
  -> Repository (db/<module>) small — CRUD and simple lookups only; returns raw rows
  -> DatabaseManager         runs your SQL
Mapper                       converts rows <-> DTOs (the only place that knows column names)
```

The provided skeletons show the shape:
`db/customer/CustomerRepository`, `mapper/CustomerMapper`,
`customer/CustomerService`, `orchestrator/AccountOpeningOrchestrator`.

## Phase 0 — Schema

Extend `DatabaseManager.createTables()`. You'll need (at least):

| Table | Key fields |
|---|---|
| `customers` | username, password_hash, role (CUSTOMER/ADMIN), first/last name, dob, email (unique), phone, address, national_id |
| `product_categories` | name, default minimumOpeningBalance / minimumOperatingBalance / interestRate / termMonths |
| `products` | category_id, name, optional overrides of the category defaults |
| `accounts` | account_number, customer_id, product_id, balance, status, opening_date |
| `payment_queue` | target_account, amount, source_rw_account, type (DEPOSIT/WITHDRAW), status (PENDING/SENT/COMPLETED/FAILED), correlation_id, reason |
| `loans` | customer_id, category, amount, rate, status, schedule info |
| `transactions` | the ledger — one row per money movement (from, to, type, amount, time) |
| `logs` | who, action, details, status, time |

Already provided: `inbox` (the messaging contract with the real-world bank).
Not in this database at all: `real_world_accounts` — it lives in the real-world
bank application's own DB (`real-world-bank/`).

Resolve product values once: **"product value if set, otherwise category default"** —
write a helper and reuse it everywhere.

## Phase 1 — Auth & authorisation

`com.bank.auth.AuthService`

```java
CustomerDTO login(String username, String password) throws InvalidCredentialsException;
void logout(Long customerId);
String hashPassword(String raw);             // BCrypt (stretch goal #3)
boolean verifyPassword(String raw, String hash);
```

- Two roles: CUSTOMER and ADMIN.
- **Authorisation is enforced in the service layer, not the menu.** A CUSTOMER
  may only ever read/act on resources they own — check ownership in the service,
  throw `UnauthorisedActionException` otherwise.

## Phase 2 — Customers

`com.bank.customer.CustomerService` (skeleton provided)

- Create profile: validate unique email, age 18+, then persist.
- View / update own profile.

## Phase 3 — Products

`com.bank.product.ProductService`

- ADMIN creates categories and products.
- Seed three categories: Savings Account, Fixed Deposit, Limited Access Account.
- Several products may exist per category (e.g. "Everyday Saver", "Bonus Saver").

## Phase 4 — Accounts

`com.bank.account.AccountService` + `orchestrator.AccountOpeningOrchestrator`

- Flow: pick category → pick product → open account.
- View portfolio (all accounts) and a single account's details/balance.

## Phase 5 — The real-world bank (separate application)

The world outside the bank is a **separate application**: `real-world-bank/` —
own database, own scheduler, own build. Read its
[README](real-world-bank/README.md) before this phase; it explains the inbox
messaging pattern and the message flow end to end.

You implement code on **both sides**:

- **Real-world side** (`com.rwbank.*` skeletons):
  - `kyc/KycService.check(...)` — account exists → KYC-verified → national ID
    matches; three distinct failure reasons. KYC lives HERE: the real-world
    bank decides whose money moves, not the requester.
  - `jobs/InboxProcessorJob.runOnce()` — claim PENDING requests, KYC, apply
    debit/credit in a transaction, write `*_RESULT` into the bank's inbox,
    mark the row DONE/FAILED.
- **Bank side** — `com.bank.realbank.RealBankService`: builds
  `DEBIT_REQUEST` / `CREDIT_REQUEST` messages and inserts them into the
  real-world inbox via `DatabaseManager.connectTo("real_world_bank.db")`.

The seeded dataset includes deliberate failure cases (unverified account, ID
mismatch, near-zero balance) — **it is the test suite for your KYC code**.

## Phase 6 — Payments (queue + messaging)

`com.bank.payment.PaymentService` + `orchestrator.PaymentProcessorOrchestrator`

- Deposit / withdraw only **add a PENDING row to `payment_queue`** — no money
  moves yet, nothing is sent yet.
- The **payment processor** (a `ScheduledJob`, also runnable from the admin
  menu) does two things per pass:
  1. **Send**: for each PENDING payment, insert a `DEBIT_REQUEST` (deposit) or
     `CREDIT_REQUEST` (withdrawal) into the real-world bank's inbox — with a
     fresh `correlation_id`, a fresh `idempotency_key`, and the payment's
     `transaction_id` — then mark the payment SENT. **Money does not move on
     send.**
  2. **Apply results**: for each PENDING `*_RESULT` in OUR inbox, find the
     SENT payment by `correlation_id`; on `outcome=OK` credit/debit the
     internal account inside a transaction + write ledger row + log and mark
     the payment COMPLETED; on `outcome=FAILED` mark it FAILED with the reason.
- Lifecycle: `PENDING → SENT → COMPLETED/FAILED`. A row stuck in SENT means
  the real-world scheduler hasn't run (or a message was lost) — that's
  visible, which is the point.
- Idempotency: claim rows with
  `UPDATE ... SET status='PROCESSING' WHERE id=? AND status='PENDING'`
  and skip if nothing was claimed; the inbox's UNIQUE `idempotency_key` makes
  redelivered messages fail to insert instead of applying twice.

### The three IDs on every message

| ID | Question it answers |
|---|---|
| `correlation_id` | Which conversation is this? Results echo it — how you match a result to its SENT payment. |
| `idempotency_key` | Has the receiver already applied this exact message? UNIQUE in the inbox. |
| `transaction_id` | Which ledger entry is this about? Carried along, echoed back. |

They sometimes hold related values — the lesson is the three questions, not
three different strings.

## Phase 7 — Transfers

`com.bank.transfer.TransferService` + `orchestrator.TransferOrchestrator`

- Between the customer's **own** accounts only.
- Apply category/product rules, e.g.:
  - Savings → must leave the minimum operating balance.
  - Fixed Deposit → not a source until maturity; not a destination once the term started.
  - Limited Access → a transfer out counts toward a yearly withdrawal limit.
- Immediate: debit source, credit destination, write ledger + log — inside a
  transaction so both sides commit together.

## Phase 8 — Loans

`com.bank.loan.LoanService` + `orchestrator.LoanOrchestrator`

- Read internal balances + real-world balance → compute max eligible amount and
  rate for the loan category (invent a simple formula).
- Requested > max → reject and set a cooldown (`LoanRejectedException`).
- Else offer; on accept, credit a chosen account and schedule equal installments.

## Phase 9 — CLI integration

Wire the `MenuDisplay` `TODO` handlers to your services/orchestrators. Keep the
CLI thin: read input, call one method, format the result (show resulting
balances). Validate input and re-prompt on bad input.

## Phase 10 — Schedulers

Neither application runs jobs on a timer. An **external scheduler** (cron,
Windows Task Scheduler, or you at a terminal) starts a short-lived process that
runs one job pass and exits:

```bash
# bank's jobs (register yours in com.bank.scheduler.SchedulerMain)
java -cp target/cli-banking-app-1.0.0.jar com.bank.scheduler.SchedulerMain payment-processor

# real-world bank's job
java -cp real-world-bank/target/real-world-bank-1.0.0.jar com.rwbank.scheduler.SchedulerMain rw-inbox-processor
```

- Implement `ScheduledJob` (`name()` + `runOnce()` returning a `JobResult`) and
  register it in the `SchedulerMain` registry. Jobs to build:
  `payment-processor` (Phase 6) and `loan-repayment` (Phase 8).
- Exit codes are the contract with the scheduler: `0` ran, `1` threw,
  `2` unknown job. Schedulers alert on non-zero — keep it honest.
- **Two applications, two schedulers, two cadences.** Run the bank's every
  minute and the real-world one every five, and watch payments sit SENT in
  between. That delay is realistic, not a bug.
- Every job must be safe to fire twice in a row (see Phase 6 idempotency) —
  cron will eventually do exactly that.

Example crontab / Task Scheduler setup (working directory = repository root):

```
* * * * *    cd /path/to/repo && java -cp target/cli-banking-app-1.0.0.jar com.bank.scheduler.SchedulerMain payment-processor
*/5 * * * *  cd /path/to/repo && java -cp real-world-bank/target/real-world-bank-1.0.0.jar com.rwbank.scheduler.SchedulerMain rw-inbox-processor
```

## Database usage

You write the SQL with `?` placeholders for every value; it runs as a
`PreparedStatement` (SQL parsed first, values bound after — a value can never
become SQL). Never concatenate values into the SQL string.

```java
DatabaseManager db = DatabaseManager.getInstance();

// SELECT
List<Map<String,Object>> rows = db.query("SELECT * FROM customers WHERE username = ?", username);

// INSERT (generated key comes back)
List<Map<String,Object>> res = db.query(
    "INSERT INTO customers (username, email) VALUES (?, ?)", username, email);
Long id = (Long) res.get(0).get("generated_key");

// Multi-statement money movement — all-or-nothing
try {
    db.startTransaction();
    db.query("UPDATE accounts SET balance = balance - ? WHERE id = ?", amount, fromId);
    db.query("UPDATE accounts SET balance = balance + ? WHERE id = ?", amount, toId);
    db.endTransaction();
} catch (SQLException e) {
    db.rollbackTransaction();
    throw e;
}

// The OTHER application's database — its inbox table ONLY, close when done
DatabaseManager rw = DatabaseManager.connectTo("real_world_bank.db");
try {
    rw.query("INSERT INTO inbox (...) VALUES (?, ?, ?, ?, ?)", /* values */);
} finally {
    rw.close();
}
```

**Careful:** a transaction cannot span both database files. "Mark payment SENT
in our DB" and "insert request in their DB" are two separate commits — a crash
between them leaves a payment SENT with no request (or the reverse). That's why
money only moves when the *result* arrives, and why rows stuck in SENT must be
visible. (SQLite's `ATTACH DATABASE` could make this atomic — deliberately not
used here; living with the gap is the lesson.)

## Mapper example (read money as BigDecimal)

```java
public static AccountDTO toDTO(Map<String,Object> row) {
    AccountDTO dto = new AccountDTO();
    dto.setId(((Number) row.get("id")).longValue());
    dto.setAccountNumber((String) row.get("account_number"));
    // money: build BigDecimal from the value, never cast to double
    dto.setBalance(new BigDecimal(String.valueOf(row.get("balance"))));
    dto.setStatus((String) row.get("status"));
    return dto;
}
```

## Custom exceptions to add

Under `com.bank.exception` (two examples already exist):
`UnauthorisedActionException`, `CustomerNotFoundException`,
`AccountNotFoundException`, `ProductNotFoundException`, `KycValidationException`,
`TransferNotAllowedException`, `LoanRejectedException`, `TransactionFailedException`.
Each carries a meaningful message and any useful context (account id, amount).

## Common pitfalls

1. Don't put business logic in `MenuDisplay` — keep it thin.
2. Enforce authorisation in services, not the menu.
3. Always validate input (null, empty, negative amounts, age, ownership).
4. Use `BigDecimal` for money; never `double`/`float`.
5. Values go through `?` placeholders, never string concatenation — `query()`
   will happily run injected SQL if you build it yourself.
6. Wrap multi-step money moves in a transaction.
7. Write a ledger row and a log for every money movement.
8. In the other application's database, touch ONLY its `inbox`. SQLite won't
   stop you reading their private tables — the discipline is the architecture.
9. Never move money when *sending* a request — only when its *result* arrives.

## Stretch goals (messaging edition)

- **Full payment lifecycle** `PENDING → SENT → COMPLETED/FAILED` with response
  callbacks both ways (this is Phase 6 done properly, end to end).
- **Suspense account** — an internal holding account. On send, move the
  customer's money into suspense (one local transaction); on `outcome=OK` move
  it on, on `outcome=FAILED` move it back. No race between a withdrawal
  in-flight and a transfer spending the same balance, because the money has
  already left the customer's account. Real banks do exactly this.
- **Idempotency keys end to end** — receiver dedupes on the UNIQUE key;
  sender treats "duplicate insert failed" as already-delivered, not an error.
- **Reconciliation job** — a `ScheduledJob` that finds payments stuck in SENT
  longer than N minutes and reports (or re-sends with the SAME idempotency
  key — safe, because the receiver dedupes).
- **Real-world interest job** — a second `com.rwbank` job that accrues interest
  on real-world balances, so source-of-wealth data moves over time.

## Submission checklist

- [ ] Functional requirements implemented (auth, products, accounts, payments, transfers, loans)
- [ ] Real-world side implemented: `KycService` + `InboxProcessorJob` (all seeded failure cases rejected with the right reason)
- [ ] Payment flow works end to end across both applications via the inboxes
- [ ] Scheduler jobs registered and runnable via both `SchedulerMain`s; safe to fire twice
- [ ] DTOs, Mappers, Orchestrators used correctly
- [ ] Custom exceptions for each error condition; no unhandled crashes
- [ ] BigDecimal money; ledger row per movement
- [ ] Seed data: products per category, sample pending deposits (real-world accounts are seeded by the provided seeder)
- [ ] JavaDoc on public classes/methods
- [ ] README with run instructions and sample accounts
- [ ] (Stretch) tests, BCrypt, config file, suspense account, reconciliation job, interest accrual
