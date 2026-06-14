package com.bank.dto;

import java.math.BigDecimal;
import com.bank.enums.AccountStatus;

public class AccountDTO {
    private Long id;
    private String accountNumber;
    private Long customerId; // the owning customer
    private Long productId; // the product this account was opened under
    private BigDecimal balance; // money is always BigDecimal, never double
    private AccountStatus status; // e.g. ACTIVE, CLOSED, MATURED
    private String openingDate;
    private boolean isLocked; // by Default False
    private String productName;
    private String category;

    /** Default constructor. */
    public AccountDTO() {
    }

    /** Constructor with all fields. */
    public AccountDTO(Long id, String accountNumber, Long customerId, Long productId,
             BigDecimal balance, AccountStatus status, String openingDate, boolean isLocked
                      ,String productName, String category
    ) {
        this.id = id;
        this.accountNumber = accountNumber;
        this.customerId = customerId;
        this.productId = productId;
        this.balance = balance;
        this.status = status;
        this.openingDate = openingDate;
        this.isLocked = isLocked;
        this.productName = productName;
        this.category = category;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
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

    public String getOpeningDate() {
        return openingDate;
    }

    public void setOpeningDate(String openingDate) {
        this.openingDate = openingDate;
    }

    public boolean getIsLocked() {
        return isLocked;
    }

    public void setIsLocked(boolean isLocked) {
        this.isLocked = isLocked;
    }

    public String getProductName() {
        return productName;
    }
    public void setProductName(String productName) {
        this.productName = productName;
    }
    public String getCategory() {
        return category;
    }
    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return "AccountDTO{" +
                "id=" + id +
                ", accountNumber='" + accountNumber + '\'' +
                ", customerId=" + customerId + '\'' +
                ", productId=" + productId + '\'' +
                ", balance=" + balance + '\'' +
                ", status='" + status + '\'' +
                ", openingDate='" + openingDate + '\'' +
                ", isLocked='" + isLocked + '\'' +
                ", productName='" + productName + '\'' +
                ", category='" + category + '\'' +
                '}';
    }
}
