package com.digitallending.loanservice.exception;

public class InvalidOtpException extends RuntimeException {
    public InvalidOtpException(String msg) {
        super(msg);
    }
}
