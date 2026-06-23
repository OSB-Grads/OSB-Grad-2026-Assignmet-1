package com.bank.mapper;
import com.bank.dto.InboxDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.sql.Timestamp;import java.util.HashMap;
import java.util.Map;
import java.time.LocalDateTime;


public class InboxMapper {
    private static final ObjectMapper mapper  = new ObjectMapper();

    public static InboxDTO toDTO(Map<String, Object> row) {

        if (row == null) {
            return null;
        }
        InboxDTO dto = new InboxDTO();
        dto.setId((String) row.get("id"));
        dto.setCorrelationId((String) row.get("correlation_id"));
        dto.setMessageType((String) row.get("message_type"));

        String payloadJson = (String) row.get("payload");
        if(payloadJson!=null && !payloadJson.isBlank()){
            Map<String, Object> payloadMap = null;
            try {
                payloadMap = mapper.readValue(payloadJson, Map.class);
            } catch (JsonProcessingException e) {
                throw new RuntimeException("Failed to parse payload JSON", e);
            }
            dto.setPayload(payloadMap);
        }

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

        Map<String,Object> payload = dto.getPayload();
        String json = null;
        try {
            json = mapper.writeValueAsString(payload);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to parse payload JSON", e);
        }
        row.put("payload", json);

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

