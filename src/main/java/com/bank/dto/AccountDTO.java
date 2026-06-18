package com.bank.dto;

import java.math.BigDecimal;
import com.bank.enums.AccountStatus;

public class AccountDTO {
    private Long id;
    private Long customerId; // the owning customer
    private Long productId; // the product this account was opened under
    private BigDecimal balance; // money is always BigDecimal, never double
    private AccountStatus status; // e.g. ACTIVE, CLOSED, MATURED
    private boolean isLocked; // by Default False

    /** Default constructor. */
    public AccountDTO() {
    }

    /** Constructor with all fields. */
    public AccountDTO(Long id,  Long customerId, Long productId,
            BigDecimal balance, AccountStatus status, boolean isLocked) {
        this.id = id;
        this.customerId = customerId;
        this.productId = productId;
        this.balance = balance;
        this.status = status;
        this.isLocked = isLocked;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
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
                ", customerId=" + customerId + '\'' +
                ", productId=" + productId + '\'' +
                ", balance=" + balance + '\'' +
                ", status='" + status + '\'' +
                ", isLocked='" + isLocked + '\'' +
                '}';
    }
}
