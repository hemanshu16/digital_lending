package com.digitallending.loanservice.exception;

public class LoanTypeNotFoundException extends RuntimeException {
    public LoanTypeNotFoundException(String s) {
        super(s);
    }
}
