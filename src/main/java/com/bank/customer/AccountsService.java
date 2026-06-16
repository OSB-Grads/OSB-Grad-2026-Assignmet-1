package com.bank.customer;
import com.bank.db.repository.AccountRepository;
import com.bank.enums.log.LogType;
import com.bank.exception.DatabaseOperationException;

import java.util.Collections;
import java.util.List;
import java.util.Map;

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
}
