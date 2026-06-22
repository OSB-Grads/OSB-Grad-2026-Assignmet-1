package com.bank.dto;
import java.time.LocalDateTime;
import java.util.Map;

public class InboxDTO {
    private String id;
    private String correlationId;
    private String messageType;
    private String payload;
    private String status;
    private String reason;
    private LocalDateTime createdAt;
    private LocalDateTime processedAt;

    public InboxDTO() {

    }

    public InboxDTO(String id,
                    String correlationId,
                    String messageType,
                    String payload,
                    String status,
                    String reason,
                    LocalDateTime createdAt,
                    LocalDateTime processedAt
                    ) {
        this.id = id;
        this.correlationId = correlationId;
        this.messageType = messageType;
        this.payload = payload;
        this.status = status;
        this.reason = reason;
        this.createdAt = createdAt;
        this.processedAt = processedAt;
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getCorrelationId() {
        return correlationId;
    }
    public void setCorrelationId(String correlationId) {
        this.correlationId = correlationId;
    }
    public String getMessageType(){
        return messageType;
    }
    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }
    public Map<String, Object> getPayload() {
        return payload;
    }
    public void setPayload(String payload) {
        this.payload = payload;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getReason() {
        return reason;
    }
    public void setReason(String reason) {
        this.reason = reason;
    }
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    public LocalDateTime getProcessedAt() {
        return processedAt;
    }
    public void setProcessedAt(LocalDateTime processedAt) {
        this.processedAt = processedAt;
    }
}
