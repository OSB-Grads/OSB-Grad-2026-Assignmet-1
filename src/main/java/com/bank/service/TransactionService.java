package com.bank.service;

import com.bank.db.repository.AccountRepository;
import com.bank.db.repository.TransactionRepository;
import com.bank.exception.InvalidAccountException;
import com.bank.utils.TransactionValidationUtils;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;


public class TransactionService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    public TransactionService() {
        this.accountRepository = new AccountRepository();
        this.transactionRepository = new TransactionRepository();
    }

    public void insertTransaction(
            long customerId,
            long fromAccountId,
            long toAccountId,
            BigDecimal amount) {

        TransactionValidationUtils.validateCustomerId(customerId); // validate the customer
        TransactionValidationUtils.validateAccountIds(fromAccountId, toAccountId); // validate the accounts
        TransactionValidationUtils.validateAmount(amount); // validate amount

        // verifying if amount exists in source account
        Map<String, Object> fromAccount = accountRepository.findAccountById(fromAccountId);
        if (fromAccount == null) {
            throw new InvalidAccountException("Source account not found");
        }
        // verifying if amount exists in Destination amount
        Map<String, Object> toAccount = accountRepository.findAccountById(toAccountId);
        if (toAccount == null) {
            throw new InvalidAccountException("Destination account not found");
        }

        // now to add this to the transaction table
        Map<String, Object> transactionFields = new HashMap<>();

        transactionFields.put("customer_id", customerId);
        transactionFields.put("from_account_id", fromAccountId);
        transactionFields.put("to_account_id", toAccountId);
        transactionFields.put("transaction_type", "TRANSFER");
        transactionFields.put("amount", amount);
        transactionFields.put("description", "Internal Transfer");
        transactionFields.put("status", "COMPLETED");
        // failing case :
        Long transactionId = transactionRepository.insert(transactionFields);
        if (transactionId == null) {
            throw new RuntimeException("failed to create transaction");
        }
    }
}