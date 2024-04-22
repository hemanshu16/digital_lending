package com.digitallending.loanservice.exception;

public class BankAccountNotFoundException extends RuntimeException {
    public BankAccountNotFoundException(String msg) {
        super(msg);
    }
}
