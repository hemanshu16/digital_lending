package com.digitallending.breservice.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExternalServiceException extends RuntimeException{
    public ExternalServiceException(String message) {
        super(message);
    }
}
