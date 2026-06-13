package com.bank.mapper;

import com.bank.dto.LogDTO;
import com.bank.enums.log.LogType;

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

        String typeStr = (String) row.get("type");
        dto.setType(typeStr != null ? LogType.valueOf(typeStr) : null);

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
        row.put("type", dto.getType() != null ? dto.getType().name() : null);
        row.put("created_at", dto.getCreatedAt());

        return row;
    }
}