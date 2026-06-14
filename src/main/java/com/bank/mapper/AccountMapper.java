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

        dto.setId((Long) row.get("id"));

        dto.setAccountNumber((String) row.get("account_number"));

        dto.setCustomerId((Long) row.get("customer_id"));

        dto.setProductId((Long) row.get("product_id"));

        dto.setBalance((BigDecimal) row.get("balance"));

        if (row.get("status") != null) {
            dto.setStatus(AccountStatus.valueOf(row.get("status").toString()));
        }

        dto.setOpeningDate((String) row.get("opening_date"));

        dto.setIsLocked((boolean) row.get("is_locked"));

        dto.setProductName((String) row.get("product_name"));

        dto.setCategory((String) row.get("category"));

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