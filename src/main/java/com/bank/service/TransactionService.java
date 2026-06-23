package com.bank.service;

import com.bank.customer.LoggerService;
import com.bank.db.repository.TransactionRepository;
import com.bank.dto.TransactionDTO;
import com.bank.enums.log.LogType;
import com.bank.exception.DatabaseOperationException;
import com.bank.exception.TransactionFailedException;
import com.bank.mapper.TransactionMapper;
import com.bank.session.Session;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final LoggerService loggerService;

    public TransactionService() {
        this.transactionRepository = new TransactionRepository();
        this.loggerService = new LoggerService();
    }

    public void insertTransaction(
            String customerId,
            String fromAccountId,
            String toAccountId,
            String transactionType,
            String description,
            BigDecimal amount) {

        try {

            Map<String, Object> transactionFields = new HashMap<>();

            transactionFields.put("customer_id", customerId);
            transactionFields.put("from_account_id", fromAccountId);
            transactionFields.put("to_account_id", toAccountId);
            transactionFields.put("transaction_type", transactionType);
            transactionFields.put("amount", amount);
            transactionFields.put("description", description);
            transactionFields.put("status", "COMPLETED");

            Long transactionId =
                    transactionRepository.insert(transactionFields);

            if (transactionId == null) {
                throw new TransactionFailedException(
                        "Failed to create transaction"
                );
            }

            loggerService.log(
                    "TRANSFER",
                    "Transfer transaction created successfully",
                    LogType.SUCCESS
            );

        } catch (Exception e) {

            loggerService.log(
                    "TRANSFER",
                    "Transfer transaction creation failed: "
                            + e.getMessage(),
                    LogType.FAILURE
            );

            throw new TransactionFailedException(
                    "Failed to create transaction", e);
        }
    }
    public List<TransactionDTO> listAccountTransactions(String accountId) {
        try {
            if (accountId == null) {
                throw new IllegalArgumentException("Account ID cannot be null");
            }
            List<Map<String, Object>> rows =
                    transactionRepository.findByAccountId(accountId);

            if(rows==null)
            {
                loggerService.log("LIST_ACCOUNT_TRANSACTIONS","No Transactions found for this account id : " + accountId,LogType.ERROR);
                return Collections.emptyList();
            }

            List<TransactionDTO> transactions =
                    rows.stream()
                            .map(TransactionMapper::toDTO)
                            .collect(Collectors.toList());

            loggerService.log(
                    "LIST_ACCOUNT_TRANSACTIONS",
                    "Account transactions retrieved successfully for account id: " + accountId,
                    LogType.SUCCESS
            );

            return transactions;

        } catch (RuntimeException e) {
            loggerService.log(
                    "LIST_ACCOUNT_TRANSACTIONS",
                    "Failed to retrieve transactions for account id: " + accountId,
                    LogType.FAILURE
            );
            throw e;
        }
    }

    public List<TransactionDTO> listCustomerTransactions(String customerId) {

        try {
            if (customerId == null) {
                throw new IllegalArgumentException("Customer ID cannot be null");
            }

            List<Map<String, Object>> rows =
                    transactionRepository.findByCustomerId(customerId);

            if (rows == null) {
                loggerService.log(
                        "LIST_CUSTOMER_TRANSACTIONS",
                        "No transactions found for customer id: " + customerId,
                        LogType.ERROR
                );

                return Collections.emptyList();
            }

            List<TransactionDTO> transactions =
                    rows.stream()
                            .map(TransactionMapper::toDTO)
                            .collect(Collectors.toList());

            loggerService.log(
                    "LIST_CUSTOMER_TRANSACTIONS",
                    "Customer transactions retrieved successfully for customer id: " + customerId,
                    LogType.SUCCESS
            );

            return transactions;

        } catch (DatabaseOperationException e) {
            loggerService.log(
                    "LIST_CUSTOMER_TRANSACTIONS",
                    "Failed to retrieve transactions for customer id: " + customerId,
                    LogType.FAILURE
            );

            throw e;
        }
    }
    public void updateTransaction(
            String customerId,
            String fromAccountId,
            String toAccountId,
            String transactionType,
            String description,
            String status,
            BigDecimal amount
    ) {
        try {
            Map<String,Object> updatedField= new HashMap<>();
            updatedField.put("customer_id", customerId);
            updatedField.put("from_account_id", fromAccountId);
            updatedField.put("to_account_id", toAccountId);
            updatedField.put("transaction_type", transactionType);
            updatedField.put("amount", amount);
            updatedField.put("description", description);
            updatedField.put("status", status);
            Integer transactionId = transactionRepository.update(customerId, updatedField);
            if(transactionId == null) {
                loggerService.log(
                        "TRANSFER",
                        "Failed to update transaction",
                        LogType.FAILURE
                );
                throw new TransactionFailedException("Failed to update transaction");
            }
            loggerService.log(
                    "TRANSFER",
                    "Transfer transaction updated successfully",
                    LogType.SUCCESS
            );
        } catch (Exception e) {
            loggerService.log(
                    "TRANSFER",
                    "Transfer transaction update failed: "
                            + e.getMessage(),
                    LogType.FAILURE
            );

            throw new TransactionFailedException(
                    "Failed to create transaction", e);
        }
    }
}