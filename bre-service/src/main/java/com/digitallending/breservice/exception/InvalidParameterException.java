package com.digitallending.breservice.exception;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class InvalidParameterException extends RuntimeException {
    public InvalidParameterException(String message) {
        super(message);
    }
}
