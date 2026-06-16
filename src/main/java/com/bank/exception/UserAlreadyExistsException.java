package com.bank.exception;

public class UserAlreadyExistsException extends Exception {
    UserAlreadyExistsException(String s)
    {
        super(s);
    }
}
