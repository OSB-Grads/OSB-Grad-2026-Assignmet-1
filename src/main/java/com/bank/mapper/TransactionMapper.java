package com.bank.mapper;

import com.bank.dto.TransactionDTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class TransactionMapper {

    public static TransactionDTO toDTO(Map<String, Object> row) {

        if (row == null) {
            return null;
        }

        TransactionDTO dto = new TransactionDTO();
        dto.setId(((String) row.get("id")));
        dto.setCustomerId(((String) row.get("customer_id")));
        dto.setFromAccountId(((String) row.get("from_account_id")));
        dto.setToAccountId(((String) row.get("to_account_id")));
        dto.setTransactionType((String) row.get("transaction_type"));
        dto.setAmount(new BigDecimal(row.get("amount").toString()));
        dto.setDescription((String) row.get("description"));
        dto.setStatus((String) row.get("status"));
        dto.setCreatedAt(LocalDateTime.parse(((String) row.get("created_at")).replace(" ", "T")));

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
        row.put("created_at",dto.getCreatedAt());

        return row;
    }
}