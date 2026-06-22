package com.bank.orchestrator;

import com.bank.customer.CustomerService;
import com.bank.service.AuthService;
import com.bank.db.DatabaseManager;
import com.bank.exception.UserAlreadyExistsException;
import com.bank.exception.UserCreationFailedException;


import java.sql.SQLException;
import com.bank.dto.AuthUserDTO;
import com.bank.dto.CustomerDTO;
import com.bank.enums.Role;
import com.bank.exception.DatabaseOperationException;
import com.bank.exception.UserCreationException;

public class SignupOrchestrator {

    private final AuthService authService;
    private final CustomerService customerService;
    private final DatabaseManager db;

    public SignupOrchestrator() {
        this.customerService = new CustomerService();
        this.authService = new AuthService();
        this.db = DatabaseManager.getInstance();
    }

    public void signup(String username, String firstName, String lastName, String dateOfBirth, String email,
            String phone,
            String address, String nationalId, String password) throws UserCreationFailedException, UserAlreadyExistsException, SQLException {
                db.startTransaction();
                String customerId= authService.signup(username,password);
                customerService.createCustomer(customerId,firstName,lastName,dateOfBirth,email,phone,address,nationalId);
                db.endTransaction();
        }
    }
