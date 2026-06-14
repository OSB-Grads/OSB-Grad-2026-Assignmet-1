package com.bank.customer;

import com.bank.db.repository.AccountRepository;
import com.bank.dto.AccountDTO;
import com.bank.mapper.AccountMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AccountsService {
    private final AccountRepository accountRepository;

    public AccountsService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public List<AccountDTO> getAllAccountsForCustomer(Long customerId) {
        List<Map<String ,Object>> rows = accountRepository.
                getAccountWithProductByCustomerId(customerId);
        List<AccountDTO> accounts = new ArrayList<>();
        if(rows == null || rows.isEmpty()) {
            return accounts;
        }
        for(Map<String,Object> row : rows) {
            accounts.add(AccountMapper.toDTO(row));
        }
        return accounts;
    }
}
