package com.bank.dto;

import com.bank.enums.log.LogType;

public class LogDTO {

    private String id;
    private String userId;
    private String action;
    private String details;
    private LogType status;
    private String createdAt;

    public LogDTO() {
    }

    public LogDTO(
            String id,
            String userId,
            String action,
            String details,
            LogType status,
            String createdAt) {

        this.id = id;
        this.userId = userId;
        this.action = action;
        this.details = details;
        this.status = status;
        this.createdAt = createdAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public LogType getStatus() {
        return status;
    }

    public void setStatus(LogType type) {
        this.status = type;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "LogDTO{" +
                "id=" + id +
                ", userId=" + userId +
                ", action='" + action + '\'' +
                ", details='" + details + '\'' +
                ", status=" + status +
                ", createdAt='" + createdAt + '\'' +
                '}';
    }
}
