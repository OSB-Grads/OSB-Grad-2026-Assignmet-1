package com.bank.exception;

public class SameAccountTransferException extends Throwable {
    public SameAccountTransferException(String message) {
        super(message);
    }
}
