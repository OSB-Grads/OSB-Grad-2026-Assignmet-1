package com.bank.mapper;

import com.bank.dto.TransactionDTO;

import java.math.BigDecimal;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class TransactionMapper {

    public static TransactionDTO toDTO(Map<String, Object> row) {

        TransactionDTO dto = new TransactionDTO();

        dto.setId((Long) row.get("id"));
        dto.setTransactionId((String) row.get("transaction_id"));
        dto.setAccountNumber((String) row.get("account_number"));
        dto.setDstAccountNumber((String) row.get("dst_account_number"));
        dto.setTransactionType((String) row.get("transaction_type"));
        dto.setStatus((String) row.get("status"));
        dto.setBalance((BigDecimal) row.get("balance"));
        return dto;
    }

    public static Map<String, Object> toRow(TransactionDTO dto) {

        Map<String, Object> row = new HashMap<>();

        row.put("id", dto.getId());
        row.put("transaction_id", dto.getTransactionId());
        row.put("account_number", dto.getAccountNumber());
        row.put("dst_account_number", dto.getDstAccountNumber());
        row.put("customer_id", dto.getCustomerId());
        row.put("transaction_type", dto.getTransactionType());
        row.put("amount", dto.getAmount());
        row.put("status", dto.getStatus());
        row.put("balance", dto.getBalance());

        return row;
    }
}