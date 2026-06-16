package com.bank.customer;

import com.bank.db.DatabaseManager;
import com.bank.db.repository.AccountRepository;
import com.bank.dto.AccountDTO;
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

    public TransferService(){
        this.repository = new AccountRepository();
        this.db = DatabaseManager.getInstance();
    }

    public void transferFunds(Long sourceAccountNumber , Long destAccountNumber, BigDecimal amount) throws SQLException,InsufficientFundsException,AccountLockedException,SameAccountTransferException,NegativeAmountException
    {
        AccountDTO srcAccountDto = AccountMapper.toDTO(repository.findAccountById(sourceAccountNumber));
        AccountDTO destAccountDto = AccountMapper.toDTO(repository.findAccountById(destAccountNumber));

        if(sourceAccountNumber.equals(destAccountNumber)){
            throw new SameAccountTransferException("Transfer Cannot happen between same Accounts");
        }
        if(amount.compareTo(BigDecimal.ZERO)<=0){
            throw new NegativeAmountException("Transfer amount should be greater than 0");
        }
        if(srcAccountDto.getBalance().compareTo(amount)<0)
        {
            throw new InsufficientFundsException("Insufficient funds in Source Account",amount,srcAccountDto.getBalance());
        }
        if(srcAccountDto.getIsLocked()) throw new AccountLockedException("Source Account is Locked");
        if(destAccountDto.getIsLocked()) throw new AccountLockedException("Destination Account is Locked");

        db.startTransaction();

        BigDecimal updatedSrcBalance =srcAccountDto.getBalance().subtract(amount);
        srcAccountDto.setBalance(updatedSrcBalance);

        BigDecimal updatedDestBalance = destAccountDto.getBalance().add(amount);
        destAccountDto.setBalance(updatedDestBalance);

        Map<String,Object> srcMap = AccountMapper.toRow(srcAccountDto);
        Map<String,Object> destMap = AccountMapper.toRow(destAccountDto);

        repository.update(sourceAccountNumber,srcMap);
        repository.update(destAccountNumber,destMap);

        db.endTransaction();
    }
}
