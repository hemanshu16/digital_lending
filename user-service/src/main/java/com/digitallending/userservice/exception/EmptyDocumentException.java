package com.digitallending.userservice.exception;

public class EmptyDocumentException extends RuntimeException {
    public EmptyDocumentException(String message) {
        super(message);
    }
}
