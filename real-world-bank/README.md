# Real-World Bank

A **separate application** simulating the world outside the bank: customers'
money held elsewhere. It has its own database (`real_world_bank.db`), its own
scheduler, and its own build — the banking application and this one are
different environments, microservices-style. The only thing they share is the
shape of one table: the `inbox`.

## How the two applications talk

No HTTP. Each application owns an `inbox` table the **other** application
inserts messages into. Each application's scheduler reads only **its own**
inbox. Everything else in each database is private — by convention (SQLite has
no permissions), so the rule is: **touch nothing but the other side's inbox**.

```
banking_app.db                          real_world_bank.db
┌──────────────────────┐               ┌──────────────────────┐
│ customers, accounts, │               │ real_world_accounts  │
│ payment_queue, ...   │               │                      │
│                      │   requests    │                      │
│            inbox ◄───┼───────────────┼──► inbox             │
│  (results land here) │   results     │ (requests land here) │
└──────────────────────┘               └──────────────────────┘
   read by bank's                          read by this app's
   scheduler jobs                          rw-inbox-processor
```

Message flow for a customer deposit of £250:

1. Banking app's payment processor INSERTs `DEBIT_REQUEST` into **this** app's
   inbox (payload `rw_account_number=RW-1001;amount=250.00;national_id=QQ123456A`).
2. This app's `rw-inbox-processor` job (run by cron or by hand) claims the row,
   runs KYC, debits the real-world account, and INSERTs `DEBIT_RESULT` into the
   **banking** app's inbox (`outcome=OK` or `outcome=FAILED;reason=...`).
3. Banking app's processor picks up the result and credits the customer —
   or marks the payment FAILED with the reason.

Withdrawals are the same dance with `CREDIT_REQUEST` / `CREDIT_RESULT`.

## The three IDs on every message

| Column | Question it answers |
|---|---|
| `correlation_id` | Which conversation does this message belong to? Results echo the request's value — that's how the sender matches a result to its waiting row. |
| `idempotency_key` | Has the receiver already applied this exact message? UNIQUE in the inbox, so a redelivered duplicate fails to INSERT instead of applying twice. Each message gets a fresh one. |
| `transaction_id` | Which business money-movement is this? The banking app's ledger identity, carried along and echoed back. |

## Running

From the **repository root** (both apps must share a working directory so the
`.db` files resolve):

```bash
mvn -f real-world-bank/pom.xml package

# one-time setup: create DB + seed reference accounts
java -jar real-world-bank/target/real-world-bank-1.0.0.jar seed

# visibility
java -jar real-world-bank/target/real-world-bank-1.0.0.jar accounts
java -jar real-world-bank/target/real-world-bank-1.0.0.jar inbox

# the scheduler — run by cron/Task Scheduler or by hand
java -cp real-world-bank/target/real-world-bank-1.0.0.jar com.rwbank.scheduler.SchedulerMain rw-inbox-processor
```

## What's provided vs what you build

**Provided (don't change):**
- `db/DatabaseManager` — schema (`real_world_accounts` + `inbox`), `query()`,
  transactions, `connectTo()` for writing into the banking app's inbox.
- `db/Seeder` — the reference dataset. It deliberately contains failure cases
  (unverified account, ID mismatch, near-zero balance...) — see its JavaDoc.
  **The dataset is your test suite**: if your code never rejects anything,
  it's wrong.
- `scheduler/` — `ScheduledJob`, `JobResult`, `SchedulerMain` (job registry +
  exit codes: 0 ran, 1 threw, 2 unknown job).
- `Main` — the `seed` / `accounts` / `inbox` commands.

**You build:**
- `kyc/KycService.check(...)` — the three KYC checks (exists, verified,
  national ID matches). Three distinct failure reasons.
- `jobs/InboxProcessorJob.runOnce()` — claim PENDING messages, KYC, apply
  debit/credit in a transaction, write the result into the banking app's
  inbox, mark the row DONE/FAILED. The claim-row UPDATE is what makes a
  double-fired scheduler harmless — see the JavaDoc.

## Why a separate scheduler?

The bank does not control when the outside world settles its payments. Two
applications, two schedulers, two cadences — run them at different intervals
and watch payments sit PENDING in between. That delay is not a bug; it's how
real interbank payments behave.
