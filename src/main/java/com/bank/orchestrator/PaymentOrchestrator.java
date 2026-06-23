package com.bank.orchestrator;

import com.bank.customer.AccountsService;
import com.bank.customer.InboxService;
import com.bank.db.DatabaseManager;
import com.bank.db.repository.AccountRepository;
import com.bank.dto.AccountDTO;
import com.bank.dto.InboxDTO;
import com.bank.service.PaymentService;
import com.bank.service.TransactionService;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class PaymentOrchestrator {
    private final DatabaseManager db;
    private final InboxService inboxService;
    private final PaymentService paymentService;
    private final AccountsService accountsService;
    private final TransactionService transactionService;
    private final AccountRepository accountRepository;

    public PaymentOrchestrator()
    {
        this.db = DatabaseManager.getInstance();
        this.inboxService = new InboxService();
        this.paymentService = new PaymentService();
        this.accountsService = new AccountsService();
        this.transactionService = new TransactionService();
        this.accountRepository = new AccountRepository();
    }

    public void processDeposits() throws SQLException {
        db.startTransaction();
        List<InboxDTO> listOfDeposits = inboxService.pickDeposits();
        for(InboxDTO processDeposit : listOfDeposits)
        {
            paymentService.deposit(processDeposit);
            Map<String,Object> payload = processDeposit.getPayload();
            AccountDTO account = accountRepository.findAccountByAccountNumber(payload.get("account_number").toString());
            transactionService.insertTransaction(
                    account.getCustomerId(),
                    null,
                    payload.get("account_number").toString(),
                    "DEPOSIT",
                    "Deposited "+payload.get("amount").toString()+"to account"+payload.get("account_number").toString(),
                    new BigDecimal(payload.get("amount").toString())
            );
            inboxService.deleteById(processDeposit.getId());
        }
        db.endTransaction();
    }

    public void processWithdrawal() throws SQLException{
       db.startTransaction();
       List<InboxDTO> listOfWithdrawal = inboxService.pickWithdrawalResponses();

       for(InboxDTO processWithdrawal : listOfWithdrawal){

            paymentService.processWithdrawal(processWithdrawal);
            Map<String,Object> payload = processWithdrawal.getPayload();
            AccountDTO account = accountRepository.findAccountByAccountNumber(payload.get("account_number").toString());
            transactionService.updateTransaction(
                    account.getCustomerId(),
                    null,
                    payload.get("account_number").toString(),
                    "WITHDRAWAL",
                    "Withdrawal Successfull  "+ payload.get("amount").toString()+"from account"+payload.get("account_number").toString(),
                    "COMPLETED",
                    new BigDecimal(payload.get("amount").toString())
            );
             inboxService.deleteById(processWithdrawal.getId());
       }
       db.endTransaction();
     }
     //TODO: revisit account identifier usage after UUID/account-number migration is finalized.
}
