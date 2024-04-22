package com.digitallending.loanservice.exception;

public class InternalServerError extends RuntimeException {
    public InternalServerError(String msg){
        super(msg);
    }
}
