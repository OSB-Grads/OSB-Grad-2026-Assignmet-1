package com.bank.orchestrator;

import com.bank.customer.CustomerService;
import com.bank.db.DatabaseManager;
import com.bank.exception.InsufficientFundsException;
import java.math.BigDecimal;
import javax.security.auth.login.AccountLockedException;

public class TransferOrchestrator {

    private final TransferService transferService;
    private final TransactionService transactionservice;
    private final DatabaseManager db;

    public TransferOrchestrator() {
        this.transferService = new TransferService();
        this.transactionservice=new TransactionService();
        this.db = DatabaseManager.getInstance();
    }

    public void transfer(Long customerId,Long sourceAccountNumber, Long destinationAccountNumber,BigDecimal amount) throws InsufficientFundsException,AccountLockedException,SameAccountTransferException,NegativeAmountException {
                db.startTransaction();
                transferService.transferFunds(sourceAccountNumber,destinationAccountNumber,amount);
                transactionservice.insertTransaction(customerId,sourceAccountNumber,destinationAccountNumber,amount);
                db.endTransaction();

        }
    }
