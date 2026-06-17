package com.bank.mapper;

import com.bank.dto.AccountDTO;
import com.bank.enums.AccountStatus;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class AccountMapper {

    public static AccountDTO toDTO(Map<String, Object> row) {

        if (row == null) {
            return null;
        }

        AccountDTO dto = new AccountDTO();

        dto.setId(((Number) row.get("id")).longValue());
        dto.setAccountNumber(((Number) row.get("account_number")).longValue());
        dto.setCustomerId(((Number) row.get("customer_id")).longValue());
        dto.setProductId(((Number) row.get("product_id")).longValue());
        dto.setBalance(row.get("balance") != null ? BigDecimal.valueOf(((Number) row.get("balance")).doubleValue()) : null);
        if (row.get("status") != null) {dto.setStatus(AccountStatus.valueOf(row.get("status").toString()));}
        dto.setOpeningDate(String.valueOf(row.get("created_at")));
        dto.setIsLocked(row.get("is_locked") != null && ((Number) row.get("is_locked")).intValue() == 1);

        return dto;
    }

    public static Map<String, Object> toRow(AccountDTO dto) {

        Map<String, Object> row = new HashMap<>();

        row.put("id", dto.getId());

        row.put("account_number", dto.getAccountNumber());

        row.put("customer_id", dto.getCustomerId());

        row.put("product_id", dto.getProductId());

        row.put("balance", dto.getBalance());

        if (dto.getStatus() != null) {
            row.put("status", dto.getStatus().toString());

        }

        row.put("opening_date", dto.getOpeningDate());

        row.put("is_locked", dto.getIsLocked());

        return row;
    }
}