package com.bank.customer;
import com.bank.db.repository.AccountRepository;
import com.bank.enums.log.LogType;
import com.bank.exception.DatabaseOperationException;

import com.bank.enums.AccountStatus;
import com.bank.dto.AccountDTO;
import com.bank.enums.log.LogType;
import com.bank.mapper.AccountMapper;
import com.bank.customer.LoggerService;

import java.math.BigDecimal;
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
        this.loggerService = new LoggerService();
    }
    public List<Map<String,Object>> getAllAccountsForCustomer(Long customerId) {
        try {
            List<Map<String ,Object>> rows = accountRepository.
                    getAccountsWithProductByCustomerId(customerId);
            if(rows == null || rows.isEmpty()) {
                loggerService.log(
                        customerId,
                        "FETCH_ALL_ACCOUNTS",
                        "No Accounts found for customer: " + customerId,
                        LogType.ERROR
                );
                return Collections.emptyList();
            }
            loggerService.log(customerId,
                    "FETCH_ALL_ACCOUNTS",
                    "Fetched all accounts for customer" + customerId,
                    LogType.SUCCESS
                    );
            return rows;
        } catch (DatabaseOperationException e) {
            loggerService.log(
                    customerId,
                    "FETCH_ALL_ACCOUNTS",
                    "Failed to fetch all accounts for customer: " + customerId +
                    ". Reason" + e.getMessage(),
                    LogType.FAILURE
            );
            throw e;
        } catch (Exception e) {
            loggerService.log(
                    customerId,
                    "FETCH_ALL_ACCOUNTS",
                    "Failed to fetch all accounts for customer: " + customerId +
                            ". Reason" + e.getMessage(),
                    LogType.FAILURE
            );
            throw e;
        }
    }

     public Long createAccount(Long customerId,Long productId ) {
        try{
            AccountDTO accountdto=new AccountDTO( null, customerId, productId, BigDecimal.ZERO, AccountStatus.ACTIVE, false);
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
