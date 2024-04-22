package com.digitallending.breservice.exception;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class InvalidWeightException extends RuntimeException{
    public InvalidWeightException(String message) {
        super(message);
    }
}
