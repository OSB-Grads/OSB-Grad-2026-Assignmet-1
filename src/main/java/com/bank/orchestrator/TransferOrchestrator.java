package com.bank.orchestrator;

import com.bank.customer.TransferService;
import com.bank.db.DatabaseManager;
import com.bank.exception.InsufficientFundsException;
import com.bank.exception.NegativeAmountException;
import com.bank.exception.SameAccountTransferException;
import com.bank.service.TransactionService;

import java.math.BigDecimal;
import java.sql.SQLException;

import javax.security.auth.login.AccountLockedException;

public class TransferOrchestrator {

    private final TransferService transferService;
    private final TransactionService transactionservice;
    private final DatabaseManager db;

    public TransferOrchestrator() {
        this.transferService = new TransferService();
        this.transactionservice = new TransactionService();
        this.db = DatabaseManager.getInstance();
    }

    public void transfer(Long customerId,
                         Long sourceAccountId,
                         Long destinationAccountId,
                         BigDecimal amount)
            throws InsufficientFundsException,
            AccountLockedException,
            SameAccountTransferException,
            NegativeAmountException,
            SQLException {

        try {
            db.startTransaction();

            transferService.transferFunds(
                    sourceAccountId,
                    destinationAccountId,
                    amount
            );

            transactionservice.insertTransaction(
                    customerId,
                    sourceAccountId,
                    destinationAccountId,
                    "TRANSFER",
                    "Internal Transfer",
                    amount
            );

            db.endTransaction();

        } catch (Exception e) {
            db.rollbackTransaction();
            throw e;
        }
    }
}
