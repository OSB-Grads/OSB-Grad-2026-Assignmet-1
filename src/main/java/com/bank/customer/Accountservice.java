package com.bank.customer;

import java.util.Map;
import com.bank.db.repository.AccountRepository;
import com.bank.dto.AccountDTO;
import com.bank.mapper.AccountMapper;

public class Accountservice {
    private final AccountRepository accountRepository;

    public Accountservice() {
        this.accountRepository = new AccountRepository();
    }

    public Long CreateAccount(Long customerId,Long productId ) {
            AccountDTO accountdto=new AccountDTO(null, null, customerId, productId, null, null, null, false);
            Map<String,Object> accountRow = AccountMapper.toRow(accountdto);
            Long accountNumber=accountRepository.insert(accountRow);
            return accountNumber;
     }
}
