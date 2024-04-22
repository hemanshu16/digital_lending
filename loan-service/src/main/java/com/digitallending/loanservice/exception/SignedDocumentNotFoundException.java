package com.digitallending.loanservice.exception;

public class SignedDocumentNotFoundException extends RuntimeException{
    public SignedDocumentNotFoundException(String msg) {
        super(msg);
    }
}
