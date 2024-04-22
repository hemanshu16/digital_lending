package com.digitallending.loanservice.exception;

public class LoanApplicationNotFoundException extends RuntimeException {
    public LoanApplicationNotFoundException(String msg){
        super(msg);
    }
}
