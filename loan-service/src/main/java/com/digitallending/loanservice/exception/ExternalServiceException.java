package com.digitallending.loanservice.exception;

public class ExternalServiceException extends RuntimeException {
    public ExternalServiceException(String msg){
        super(msg);
    }
}
