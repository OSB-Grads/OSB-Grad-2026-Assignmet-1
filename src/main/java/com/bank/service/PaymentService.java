package com.bank.service;

import com.bank.customer.LoggerService;
import com.bank.db.repository.AccountRepository;
import com.bank.dto.InboxDTO;
import com.bank.enums.log.LogType;

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
            String accountNumber = payload.get("accountNumber").toString(); // getting the account_number
            BigDecimal amount = new BigDecimal(payload.get("amount").toString()); // getting the amount
            Map<String, Object> account = accountRepository.findAccountByAccountNumber(accountNumber);
            if (account == null) {

                loggerService.log(
                        "DEPOSIT",
                        "Account not found: " + accountId,
                        LogType.ERROR
                );

                throw new IllegalArgumentException(
                        "Account not found"
                );
            }
            BigDecimal currentBalance = (BigDecimal) account.get("balance");
            account.put("balance", currentBalance.add(amount)); // adding
            accountRepository.update(accountNumber, account); // updating

            loggerService.log(
                    "DEPOSIT",
                    "Deposit successful for account "
                            + accountId
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
}