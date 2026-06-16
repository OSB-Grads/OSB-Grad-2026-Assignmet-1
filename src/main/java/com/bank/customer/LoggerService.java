package com.bank.customer;

import com.bank.db.repository.LogRepository;
import com.bank.enums.log.LogType;
import com.bank.exception.DatabaseOperationException;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class LoggerService {
    private final LogRepository logRepository;

    public LoggerService() {
        this.logRepository = new LogRepository();
    }

    public void log(Long customerId,String action, String details, LogType status) {
        Map<String, Object> logFields = new HashMap<>();
        logFields.put("user_id", customerId);
        logFields.put("action", action);
        logFields.put("details",details);
        logFields.put("status", status.name());

        try {
            logRepository.create(logFields);
        }catch (SQLException e) {
            throw new DatabaseOperationException(
                    "Failed to create log entry",
                    e
            );
        }
    }
}
