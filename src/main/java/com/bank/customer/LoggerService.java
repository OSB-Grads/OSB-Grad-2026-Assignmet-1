package com.bank.customer;

import com.bank.db.repository.LogRepository;
import com.bank.enums.log.LogType;
import com.bank.exception.DatabaseOperationException;
import com.bank.session.Session;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class LoggerService {
    private final LogRepository logRepository;
    private final Session session;

    public LoggerService() {
        this.logRepository = new LogRepository();
        this.session = Session.getInstance();
    }

    public void log(String action, String details, LogType status) {
        Map<String, Object> logFields = new HashMap<>();
        logFields.put("customer_id", session.getCustomerId());
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
