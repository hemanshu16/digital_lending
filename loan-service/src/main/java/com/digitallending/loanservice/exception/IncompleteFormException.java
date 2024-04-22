package com.digitallending.loanservice.exception;

public class IncompleteFormException extends RuntimeException {
    public IncompleteFormException(String msg) {
        super(msg);
    }
}
