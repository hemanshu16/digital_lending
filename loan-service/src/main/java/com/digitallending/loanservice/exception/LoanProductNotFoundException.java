package com.digitallending.loanservice.exception;

public class LoanProductNotFoundException extends RuntimeException {
    public LoanProductNotFoundException(String msg) {
        super(msg);
    }
}
