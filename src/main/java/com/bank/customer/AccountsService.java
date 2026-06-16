package com.bank.customer;

import com.bank.db.repository.AccountRepository;
import com.bank.dto.AccountDTO;
import com.bank.enums.log.LogType;
import com.bank.mapper.AccountMapper;
import com.bank.customer.LoggerService;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import com.bank.exception.DatabaseOperationException;


public class AccountsService {
    private final AccountRepository accountRepository;
    private final LoggerService loggerService;

    public AccountsService() {
        this.accountRepository = new AccountRepository();
        this.loggerService=new LoggerService();
    }
    public List<Map<String,Object>> getAllAccountsForCustomer(Long customerId) {
        List<Map<String ,Object>> rows = accountRepository.
                getAccountsWithProductByCustomerId(customerId);
        if(rows == null || rows.isEmpty()) {
            return null;
        }
        return rows;
    }

     public Long createAccount(Long customerId,Long productId ) {
        try{
            AccountDTO accountdto=new AccountDTO(null, null, customerId, productId, null, null, null, false);
            Map<String,Object> accountRow = AccountMapper.toRow(accountdto);
            Long accountNumber=accountRepository.insert(accountRow);
            loggerService.log(customerId,
                    "CREATE_ACCOUNT",
                    "Created Account for the product "+productId,
                    LogType.SUCCESS
                    );
            return accountNumber;
        }
        catch(DatabaseOperationException e){
                       loggerService.log(
                    customerId,
                    "CREATE_ACCOUNT",
                    "Failed to Create Account",
                    LogType.FAILURE
            );
            throw new DatabaseOperationException("Failed to Create Account");
        }catch(Exception e){
                       loggerService.log(
                    customerId,
                    "CREATE_ACCOUNT",
                    "Failed to do Create Account Operation. Reason:"+e.getMessage(),
                    LogType.FAILURE
            );
            throw e;
        }
     }
}
