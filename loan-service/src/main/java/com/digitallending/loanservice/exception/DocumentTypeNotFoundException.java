package com.digitallending.loanservice.exception;

public class DocumentTypeNotFoundException extends RuntimeException {
    public DocumentTypeNotFoundException(String msg) {
        super(msg);
    }
}
