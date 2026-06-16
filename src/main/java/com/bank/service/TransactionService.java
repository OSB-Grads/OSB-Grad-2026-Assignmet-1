package com.bank.service;

import com.bank.customer.LoggerService;
import com.bank.db.repository.TransactionRepository;
import com.bank.enums.log.LogType;
import com.bank.exception.TransactionFailedException;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final LoggerService loggerService;

    public TransactionService() {
        this.transactionRepository = new TransactionRepository();
        this.loggerService = new LoggerService();
    }

    public void insertTransaction(
            long customerId,
            long fromAccountId,
            long toAccountId,
            BigDecimal amount) {

        try {

            Map<String, Object> transactionFields = new HashMap<>();

            transactionFields.put("customer_id", customerId);
            transactionFields.put("from_account_id", fromAccountId);
            transactionFields.put("to_account_id", toAccountId);
            transactionFields.put("transaction_type", "TRANSFER");
            transactionFields.put("amount", amount);
            transactionFields.put("description", "Internal Transfer");
            transactionFields.put("status", "COMPLETED");

            Long transactionId =
                    transactionRepository.insert(transactionFields);

            if (transactionId == null) {
                throw new TransactionFailedException(
                        "Failed to create transaction"
                );
            }

            loggerService.log(
                    customerId,
                    "TRANSFER",
                    "Transfer transaction created successfully",
                    LogType.SUCCESS
            );

        } catch (Exception e) {

            loggerService.log(
                    customerId,
                    "TRANSFER",
                    "Transfer transaction creation failed: "
                            + e.getMessage(),
                    LogType.FAILURE
            );

            throw new TransactionFailedException(
                    "Failed to create transaction", e);
        }
    }
}