package com.digitallending.userservice.exception;

public class InvalidMobileNumberException extends RuntimeException {
    public InvalidMobileNumberException(String message) {
        super(message);
    }
}
