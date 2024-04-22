package com.digitallending.breservice.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmptyPayloadException extends RuntimeException{
    public EmptyPayloadException(String message) {
        super(message);
    }
}
