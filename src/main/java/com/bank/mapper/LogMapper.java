package com.bank.mapper;

import com.bank.dto.LogDTO;

import java.util.HashMap;
import java.util.Map;

public class LogMapper {

    public static LogDTO toDTO(Map<String, Object> row) {

        LogDTO dto = new LogDTO();
        dto.setId(
                row.get("id") != null
                        ? ((Long) row.get("id"))
                        : null
        );
        dto.setUserId(
                row.get("user_id") != null
                        ? ((Long) row.get("user_id"))
                        : null
        );
        dto.setAction((String) row.get("action"));
        dto.setDetails((String) row.get("details"));
        dto.setType((String) row.get("type"));

        dto.setCreatedAt(row.get("created_at") != null
                        ? row.get("created_at").toString()
                        : null
        );
        return dto;
    }

    public static Map<String, Object> toRow(LogDTO dto) {

        Map<String, Object> row = new HashMap<>();

        row.put("id", dto.getId());
        row.put("user_id", dto.getUserId());
        row.put("action", dto.getAction());
        row.put("details", dto.getDetails());
        row.put("type", dto.getType());
        row.put("created_at", dto.getCreatedAt());

        return row;
    }
}