package com.digitallending.loanservice.exception;

public class LoanRequestNotFoundException extends RuntimeException{
    public LoanRequestNotFoundException(String msg) {
        super(msg);
    }
}
