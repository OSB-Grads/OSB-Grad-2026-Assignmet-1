
package com.bank.utils;

import com.bank.exception.*;

import java.math.BigDecimal;

public final class TransactionValidationUtils {

    private TransactionValidationUtils() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static void validateCustomerId(Long customerId) {
        if (customerId == null) {
            throw new InvalidCustomerException("Customer ID is required");
        }
    }

    public static void validateAccountIds(Long fromAccountId, Long toAccountId) {
        if (fromAccountId == null || toAccountId == null ||) {
            throw new InvalidAccountException("Account IDs must not be null or empty");
        }
        if (fromAccountId<=0||toAccountId<=0){
            throw new InvalidAccountException("Account IDs must be positive numbers");
        }
        if (fromAccountId.equals(toAccountId)) {
            throw new SameAccountTransferException("From and To account cannot be the same");
        }
    }

    public static void validateAmount(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidAmountException("Amount must be greater than zero");
        }
    }
}
