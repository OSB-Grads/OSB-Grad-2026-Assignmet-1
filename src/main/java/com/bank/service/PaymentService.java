package com.bank.service;

import com.bank.customer.LoggerService;
import com.bank.db.repository.AccountRepository;
import com.bank.dto.AccountDTO;
import com.bank.dto.InboxDTO;
import com.bank.enums.log.LogType;
import com.bank.mapper.AccountMapper;

import java.math.BigDecimal;
import java.util.Map;

public class PaymentService {

    private final AccountRepository accountRepository;
    private final LoggerService loggerService;

    public PaymentService() {
        this.accountRepository = new AccountRepository();
        this.loggerService = new LoggerService();
    }

    public void deposit(InboxDTO inboxDTO) {

        try {
            // base case
            if (inboxDTO == null || inboxDTO.getPayload() == null) {
                loggerService.log(
                        "DEPOSIT",
                        "Invalid deposit payload",
                        LogType.ERROR
                );
                throw new IllegalArgumentException("Invalid deposit payload");
            }

            Map<String, Object> payload = inboxDTO.getPayload();
            String accountNumber = payload.get("account_number").toString(); // getting the account_number
            BigDecimal amount = new BigDecimal(payload.get("amount").toString()); // getting the amount
            AccountDTO account = accountRepository.findAccountByAccountNumber(accountNumber);

            if (account == null) {

                loggerService.log(
                        "DEPOSIT",
                        "Account not found: " + accountNumber,
                        LogType.ERROR
                );

                throw new IllegalArgumentException(
                        "Account not found"
                );
            }
            Map<String,Object> accountMap = AccountMapper.toRow(account);

            BigDecimal currentBalance = (BigDecimal) accountMap.get("balance");
            accountMap.put("balance", currentBalance.add(amount)); // adding

            accountRepository.update(accountNumber, accountMap); // updating

            loggerService.log(
                    "DEPOSIT",
                    "Deposit successful for account "
                            + accountNumber
                            + ". Amount: "
                            + amount,
                    LogType.SUCCESS
            );

        } catch (Exception e) {

            loggerService.log(
                    "DEPOSIT",
                    "Failed to process deposit. Reason: "
                            + e.getMessage(),
                    LogType.FAILURE
            );

            throw e;
        }
    }

    public void processWithdrawal(InboxDTO inboxDTO)
    {

        try{
            if(inboxDTO==null || inboxDTO.getPayload()==null)
            {
                loggerService.log(
                        "WITHDRAWAL",
                        "Invalid Withdrawal payload",
                        LogType.ERROR
                );
                throw new IllegalArgumentException("Invalid Withdrawal payload");
            }
            Map<String,Object> payload = inboxDTO.getPayload();
            String accountNumber = payload.get("account_number").toString();
            BigDecimal amount = new BigDecimal(payload.get("amount").toString());

            AccountDTO account = accountRepository.findAccountByAccountNumber(accountNumber);
            if(account == null)
            {
                loggerService.log(
                        "WITHDRAWAL",
                        "Account Not Found for accNumber "+accountNumber,
                        LogType.FAILURE
                );
                throw new IllegalArgumentException("Account not found.");
            }
            Map<String,Object> accountMap = AccountMapper.toRow(account);

            BigDecimal currentBalance = (BigDecimal) accountMap.get("balance");
            accountMap.put("balance",currentBalance.subtract(amount));
            accountRepository.update(accountNumber,accountMap);

            loggerService.log(
                    "WITHDRAWAL",
                    "Withdrawal successful for Account "+accountNumber+
                            " for Amount "+amount,
                    LogType.SUCCESS
            );
        }catch(Exception e)
        {
           loggerService.log(
                   "WITHDRAWAL",
                   "Failed to process withdrawal "+ e.getMessage(),
                   LogType.FAILURE
           );
           throw e;
        }
    }
}