package com.bank.mapper;

import com.bank.dto.AuthUserDTO;
import com.bank.enums.Role;

import java.util.HashMap;
import java.util.Map;

public class AuthMapper {

    public static AuthUserDTO toDTO(Map<String, Object> row) {

        if (row == null) {
            return null;
        }

        AuthUserDTO dto = new AuthUserDTO();

        dto.setId((java.math.BigDecimal) row.get("id"));
        dto.setUsername((String) row.get("username"));
        dto.setPasswordHash((String) row.get("password_hash"));
        dto.setCustomerId((java.math.BigDecimal) row.get("customer_id"));

        if (row.get("role") != null) {
            dto.setRole(Role.valueOf(row.get("role").toString()));
        }

        return dto;
    }

    public static Map<String, Object> toRow(AuthUserDTO dto) {

        Map<String, Object> row = new HashMap<>();

        row.put("id", dto.getId());
        row.put("username", dto.getUsername());
        row.put("password_hash", dto.getPasswordHash());
        row.put("customer_id", dto.getCustomerId());

        if (dto.getRole() != null) {
            row.put("role", dto.getRole().name());
        }

        return row;
    }
}