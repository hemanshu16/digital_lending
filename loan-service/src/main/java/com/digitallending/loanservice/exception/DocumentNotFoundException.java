package com.digitallending.loanservice.exception;

public class DocumentNotFoundException extends RuntimeException {
    public DocumentNotFoundException(String msg) {
        super(msg);
    }
}
