package com.bank.mapper;

import com.bank.dto.TransactionDTO;

import java.util.HashMap;
import java.util.Map;

public class TransactionMapper {

    public static TransactionDTO toDTO(Map<String, Object> row) {

        if (row == null) {
            return null;
        }

        TransactionDTO dto = new TransactionDTO();
        dto.setId((Long) row.get("id"));
        dto.setCustomerId((Long) row.get("customer_id"));
        dto.setFromAccountId((Long) row.get("from_account_id"));
        dto.setToAccountId((Long) row.get("to_account_id"));
        dto.setTransactionType((String) row.get("transaction_type"));
        dto.setAmount((java.math.BigDecimal) row.get("amount"));
        dto.setDescription((String) row.get("description"));
        dto.setStatus((String) row.get("status"));

        return dto;
    }

    public static Map<String, Object> toRow(TransactionDTO dto) {

        Map<String, Object> row = new HashMap<>();

        row.put("id", dto.getId());
        row.put("customer_id", dto.getCustomerId());
        row.put("from_account_id", dto.getFromAccountId());
        row.put("to_account_id", dto.getToAccountId());
        row.put("transaction_type", dto.getTransactionType());
        row.put("amount", dto.getAmount());
        row.put("description", dto.getDescription());
        row.put("status", dto.getStatus());

        return row;
    }
}