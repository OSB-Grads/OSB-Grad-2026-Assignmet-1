package com.bank.dto;

import java.math.BigDecimal;

/**
 * Data Transfer Object for Account information.
 *
 * <p>Carries account data between layers so that higher layers (services, CLI)
 * never see the raw database entity. Fields follow the assignment spec: an
 * account has an account number, an owning customer, a product, a balance,
 * a status, and an opening date.</p>
 *
 * <p>Note: the account's type/rates are NOT stored here directly &mdash; they
 * come from the {@code Product} (and its {@code Product Category} default).
 * This DTO only references the product by name/id; resolve product values
 * ("product value if set, otherwise category default") in the service layer.</p>
 */
public class AccountDTO {
    private Long id;
    private String accountNumber;
    private Long customerId;        // the owning customer
    private Long productId;         // the product this account was opened under
    private BigDecimal balance;     // money is always BigDecimal, never double
    private String status;          // e.g. ACTIVE, CLOSED, MATURED
    private String openingDate;
    private boolean isLocked;       // by Default False

    /** Default constructor. */
    public AccountDTO() {}

    /** Constructor with all fields. */
    public AccountDTO(Long id, String accountNumber, Long customerId, Long productId,
                      String productName, BigDecimal balance, String status, String openingDate, boolean isLocked) {
        this.id = id;
        this.accountNumber = accountNumber;
        this.customerId = customerId;
        this.productId = productId;
        this.balance = balance;
        this.status = status;
        this.openingDate = openingDate;
        this.isLocked=isLocked;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOpeningDate() {
        return openingDate;
    }

    public void setOpeningDate(String openingDate) {
        this.openingDate = openingDate;
    }

    public boolean getIsLocked(){
        return isLocked;
    }

    public void setIsLocked(boolean isLocked){
        this.isLocked=isLocked;
    }

    @Override
    public String toString() {
        return "AccountDTO{" +
                "id=" + id +
                ", accountNumber='" + accountNumber + '\'' +
                ", customerId=" + customerId + '\''+
                ", productId=" + productId +'\''+
                ", balance=" + balance +'\''+
                ", status='" + status + '\'' +
                ", openingDate='" + openingDate + '\'' +
                ", isLocked='" + isLocked +'\'' +
                '}';
    }
}
