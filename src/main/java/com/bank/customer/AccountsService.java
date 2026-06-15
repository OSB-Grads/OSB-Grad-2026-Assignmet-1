package com.bank.customer;

import com.bank.db.repository.AccountRepository;
import com.bank.dto.AccountDTO;
import com.bank.mapper.AccountMapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AccountsService {
    private final AccountRepository accountRepository;

    public AccountsService() {
        this.accountRepository = new AccountRepository();
    }
    public List<Map<String,Object>> getAllAccountsForCustomer(Long customerId) {
        List<Map<String ,Object>> rows = accountRepository.
                getAccountWithProductByCustomerId(customerId);
        if(rows == null || rows.isEmpty()) {
            return null;
        }
        return rows;
    }
}
