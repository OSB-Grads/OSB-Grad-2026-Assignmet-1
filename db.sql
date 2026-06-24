-- UPDATE ACCOUNT BALANCE
-- =========================================================

UPDATE accounts
SET balance = 0.00
WHERE customer_id = '';

-- =========================================================
-- MAKE USER ADMIN
-- =========================================================

UPDATE auth
SET role = 'ADMIN'
WHERE username = 'Admin';

-- =========================================================
-- CLEANUP QUERIES
-- =========================================================

DELETE FROM accounts;

DELETE FROM inbox;

DELETE FROM transactions;

DELETE FROM products;

-- =========================================================
-- DEPOSIT TEST MESSAGE TEMPLATE
-- =========================================================

INSERT INTO inbox (
id,
correlation_id,
transaction_id,
message_type,
payload,
status,
reason
)
VALUES (
'',
'',
'',
'DEPOSITS',
'{"account_number":"","amount":0.00}',
'PENDING',
''
);

-- View Deposit Messages

SELECT *
FROM inbox
WHERE message_type = 'DEPOSITS';

-- =========================================================
-- WITHDRAWAL RESPONSE TEMPLATE
-- =========================================================

INSERT INTO inbox (
id,
correlation_id,
message_type,
payload,
status,
reason
)
VALUES (
'',
'',
'WITHDRAWAL_RESPONSE',
'{"account_number":"","amount":0.00,"transaction_id":""}',
'PENDING',
''
);

-- View Withdrawal Messages

SELECT *
FROM inbox
WHERE message_type = 'WITHDRAWAL_RESPONSE';



-- =========================================================
-- CHECK DATA
-- =========================================================

SELECT * FROM accounts;

SELECT * FROM transactions;

SELECT * FROM inbox;

SELECT * FROM products;

-- =========================================================
-- DROP TABLES (USE CAREFULLY)
-- =========================================================

DROP TABLE inbox;

DROP TABLE transactions;