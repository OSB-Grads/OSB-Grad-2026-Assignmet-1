package com.bank.dto;

import com.bank.enums.log.LogType;

public class LogDTO {

    private Long id;
    private Long userId;
    private String action;
    private String details;
    private LogType type;
    private String createdAt;

    public LogDTO() {
    }

    public LogDTO(
            Long id,
            Long userId,
            String action,
            String details,
            LogType type,
            String createdAt) {

        this.id = id;
        this.userId = userId;
        this.action = action;
        this.details = details;
        this.type = type;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
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

    public LogType getType() {
        return type;
    }

    public void setType(LogType type) {
        this.type = type;
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
                ", type=" + type +
                ", createdAt='" + createdAt + '\'' +
                '}';
    }
}
