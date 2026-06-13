package com.bank.mapper;
import com.bank.dto.InboxDTO;
import java.sql.Timestamp;import java.util.HashMap;
import java.util.Map;
import java.time.LocalDateTime;

public class InboxMapper {
    public static InboxDTO toDTO(Map<String, Object> row) {

        if (row == null) {
            return null;
        }
        InboxDTO dto = new InboxDTO();
        dto.setId(((Number) row.get("id")).longValue());
        dto.setCorrelationId((String) row.get("correlation_id"));
        dto.setMessageType((String) row.get("message_type"));
        dto.setPayload((String) row.get("payload"));
        dto.setStatus((String) row.get("status"));
        dto.setReason((String) row.get("reason"));
        Object createdAt = row.get("created_at");
        if (createdAt instanceof Timestamp) {
            dto.setCreatedAt(((Timestamp) createdAt).toLocalDateTime());
        }
        Object processedAt = row.get("processed_at");
        if (processedAt instanceof Timestamp) {
            dto.setProcessedAt(((Timestamp) processedAt).toLocalDateTime());
        }
        return dto;
    }

    public static Map<String, Object> toRow(InboxDTO dto) {
        if (dto == null) {
            return null;
        }
        Map<String, Object> row = new HashMap<>();
        row.put("id", dto.getId());
        row.put("correlation_id", dto.getCorrelationId());
        row.put("message_type", dto.getMessageType());
        row.put("payload", dto.getPayload());
        row.put("status", dto.getStatus());
        row.put("reason", dto.getReason());
        LocalDateTime createdAt = dto.getCreatedAt();
        if (createdAt != null) {
            row.put("created_at", Timestamp.valueOf(createdAt));
        }
        LocalDateTime processedAt = dto.getProcessedAt();
        if (processedAt != null) {
            row.put("processed_at", Timestamp.valueOf(processedAt));
        }
        return row;
    }
}

