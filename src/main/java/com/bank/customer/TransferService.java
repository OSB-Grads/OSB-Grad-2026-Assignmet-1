package com.bank.customer;

import com.bank.db.DatabaseManager;
import com.bank.db.repository.AccountRepository;
import com.bank.dto.AccountDTO;
import com.bank.enums.log.LogType;
import com.bank.exception.InsufficientFundsException;
import com.bank.exception.NegativeAmountException;
import com.bank.exception.SameAccountTransferException;
import com.bank.mapper.AccountMapper;

import javax.security.auth.login.AccountLockedException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Map;


public class TransferService {

    private final AccountRepository repository;
    private final DatabaseManager db;
    private final LoggerService loggerService;

    public TransferService(){
        this.repository = new AccountRepository();
        this.db = DatabaseManager.getInstance();
        this.loggerService = new LoggerService();
    }

    public void transferFunds(String sourceAccountNumber , String destAccountNumber, BigDecimal amount) throws SQLException,InsufficientFundsException,AccountLockedException,SameAccountTransferException,NegativeAmountException
    {
        AccountDTO srcAccountDto = AccountMapper.toDTO(repository.findAccountById(sourceAccountNumber));
        AccountDTO destAccountDto = AccountMapper.toDTO(repository.findAccountById(destAccountNumber));

        if(sourceAccountNumber.equals(destAccountNumber)){
            loggerService.log(
                    "TRANSFER_BETWEEN_ACCOUNTS",
                    "Same Account Transfer not possible",
                    LogType.FAILURE
            );
            throw new SameAccountTransferException("Transfer Cannot happen between same Accounts");
        }
        if(amount.compareTo(BigDecimal.ZERO)<=0){
            loggerService.log(
                    "TRANSFER_BETWEEN_ACCOUNTS",
                    "Cannot transfer invalid amount",
                    LogType.FAILURE
            );
            throw new NegativeAmountException("Transfer amount should be greater than 0");
        }
        if(srcAccountDto.getBalance().compareTo(amount)<0)
        {
            loggerService.log(
                    "TRANSFER_BETWEEN_ACCOUNTS",
                    "Insufficient funds in the source Account",
                    LogType.FAILURE
            );
            throw new InsufficientFundsException("Insufficient funds in Source Account",amount,srcAccountDto.getBalance());
        }
        if(srcAccountDto.getIsLocked()){
            loggerService.log(
                    "TRANSFER_BETWEEN_ACCOUNTS",
                    "Source Account is Locked",
                    LogType.FAILURE
            );
            throw new AccountLockedException("Source Account is Locked");
        }
        if(destAccountDto.getIsLocked()) {
            loggerService.log(
                    "TRANSFER_BETWEEN_ACCOUNTS",
                    "Destination Account is Locked",
                    LogType.FAILURE
            );
            throw new AccountLockedException("Destination Account is Locked");
        }
        db.startTransaction();

        BigDecimal updatedSrcBalance =srcAccountDto.getBalance().subtract(amount);
        srcAccountDto.setBalance(updatedSrcBalance);

        BigDecimal updatedDestBalance = destAccountDto.getBalance().add(amount);
        destAccountDto.setBalance(updatedDestBalance);

        System.out.println("\nSource new balance = " + srcAccountDto.getBalance());
        System.out.println("Destination new balance = " + destAccountDto.getBalance());

        Map<String,Object> srcMap = AccountMapper.toRow(srcAccountDto);
        Map<String,Object> destMap = AccountMapper.toRow(destAccountDto);

        repository.update(srcAccountDto.getId(), srcMap);
        repository.update(destAccountDto.getId(),destMap);

        loggerService.log(
                    "TRANSFER_BETWEEN_ACCOUNTS",
                    "Amount transferred Succesfully",
                    LogType.SUCCESS
            );
            
        db.endTransaction();
    }
}
