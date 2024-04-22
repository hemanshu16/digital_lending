package com.digitallending.breservice.exception;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class InvalidValueException extends RuntimeException{
    public InvalidValueException(String message) {
        super(message);
    }
}
