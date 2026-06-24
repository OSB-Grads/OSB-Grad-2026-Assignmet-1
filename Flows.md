# Flow Checklist

This document tracks the implementation status of end-to-end business flows defined in the PRD.

## Authentication Flows

* [x] Customer Registration
* [x] Customer Login
* [x] Customer Logout
* [x] Admin Login
* [ ] Authorization Enforcement

---

## Customer Flows

* [x] Create Customer Profile
* [x] View Customer Profile
* [x] Update Customer Profile

---

## Product Management Flows

* [x] View Product Categories
* [x] View Products by Category
* [x] Create Product (Admin)

---

## Account Management Flows

* [x] Open Account (Category → Product → Account)
* [x] View Customer Portfolio
* [x] View Individual Account Details

---

## Payment Flows

### Deposit

* [x] Create Deposit Request
* [x] Queue Deposit Request
* [x] Process Deposit Request
* [ ] Deposit Failure Handling

### Withdrawal

* [x] Create Withdrawal Request
* [x] Queue Withdrawal Request
* [x] Process Withdrawal Request
* [ ] Withdrawal Failure Handling

---

## Transfer Flows

* [x] Transfer Between Own Accounts
* [x] Ownership Validation
* [x] Balance Validation
* [ ] Product Rule Validation
* [ ] Transfer Failure Handling

---

## Real-World Bank Flows

* [ ] Seed Real-World Accounts
* [ ] KYC Validation
* [ ] National ID Verification
* [ ] External Account Balance Validation

---

## Loan Flows

* [ ] Loan Eligibility Calculation
* [ ] Loan Request
* [ ] Loan Offer
* [ ] Loan Acceptance
* [ ] Loan Rejection
* [ ] Loan Repayment Processing

---

## Transaction & Audit Flows

* [x] View Transaction History
* [ ] Transaction Ledger Creation
* [ ] Deposit Audit Trail
* [ ] Withdrawal Audit Trail
* [ ] Transfer Audit Trail

---

## Admin Flows

* [x] Manage Products
* [x] Run Payment Processor
* [ ] Seed Real-World Accounts
* [ ] Run Loan Repayments
* [ ] View Logs

---

## Integration Flows

* [x] Deposit → Process Deposit
* [x] Withdraw → Process Withdrawal
* [x] Deposit → Process → Withdraw → Process
* [x] Open Account → Deposit → Transfer
* [ ] Open Account → Deposit → Withdraw → Process
* [ ] Loan Request → Approval → Credit Account

---

## Progress Summary

### Completed Flows

21

### Remaining Flows

20

### Overall Progress

21 / 41
