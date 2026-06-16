package com.bank.orchestrator;

import com.bank.customer.CustomerService;
import com.bank.db.DatabaseManager;
import com.bank.dto.AuthUserDTO;
import com.bank.dto.CustomerDTO;
import com.bank.dto.SignupDTO;
import com.bank.enums.Role;
import com.bank.exception.DatabaseOperationException;
import com.bank.exception.UserCreationException;
import com.bank.exception.UserCreationFailedException;

public class SignupOrchestrator {

    private final AuthService authservice;
    private final CustomerService customerService;
    private final DatabaseManager db;

    public SignupOrchestrator() {
        this.customerService = new CustomerService();
        this.authservice = new AuthService();
        this.db = new DatabaseManager();
    }

    public void signup(String username, String firstName, String lastName, String dateOfBirth, String email,
            String phone,
            String address, String nationalId, String password) throws UserCreationFailedException, UserAlreadyExistsException {
                db.startTransaction();
                Long customerId= AuthService.signup(username,password);
                CustomerService.createCustomer(customerId,firstName,lastName,dateOfBirth,email,phone,address,nationalId);
                db.endTransaction();
        }
    }
