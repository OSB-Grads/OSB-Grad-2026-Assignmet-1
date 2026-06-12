package com.bank.dto;

public class LogDTO {

    private Long id;
    private Long userId;
    private String action;
    private String details;
    private String ipAddress;
    private String status;
    private String createdAt;

    public LogDTO() {
    }

    public LogDTO(
            Long id,
            Long userId,
            String action,
            String details,
            String ipAddress,
            String status,
            String createdAt) {

        this.id = id;
        this.userId = userId;
        this.action = action;
        this.details = details;
        this.ipAddress = ipAddress;
        this.status = status;
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

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
                ", ipAddress='" + ipAddress + '\'' +
                ", status='" + status + '\'' +
                ", createdAt='" + createdAt + '\'' +
                '}';
    }
}
