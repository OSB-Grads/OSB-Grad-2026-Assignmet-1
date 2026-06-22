package com.bank.dto;

import java.math.BigDecimal;
import com.bank.enums.AccountStatus;

public class AccountDTO {
    private String id;
    private String accountNumber;
    private String customerId; // the owning customer
    private String productId; // the product this account was opened under
    private BigDecimal balance; // money is always BigDecimal, never double
    private AccountStatus status; // e.g. ACTIVE, CLOSED, MATURED
    private boolean isLocked; // by Default False

    /** Default constructor. */
    public AccountDTO() {
    }

    /** Constructor with all fields. */
    public AccountDTO(String id, String accountNumber, String customerId, String productId,
            BigDecimal balance, AccountStatus status, boolean isLocked) {
        this.id = id;
        this.accountNumber = accountNumber;
        this.customerId = customerId;
        this.productId = productId;
        this.balance = balance;
        this.status = status;
        this.isLocked = isLocked;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public AccountStatus getStatus() {
        return status;
    }

    public void setStatus(AccountStatus status) {
        this.status = status;
    }

    public boolean getIsLocked() {
        return isLocked;
    }

    public void setIsLocked(boolean isLocked) {
        this.isLocked = isLocked;
    }

    @Override
    public String toString() {
        return "AccountDTO{" +
                "id=" + id +
                "accountNumber="+accountNumber+'\''+
                ", customerId=" + customerId + '\'' +
                ", productId=" + productId + '\'' +
                ", balance=" + balance + '\'' +
                ", status='" + status + '\'' +
                ", isLocked='" + isLocked + '\'' +
                '}';
    }
}
