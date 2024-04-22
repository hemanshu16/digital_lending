package com.digitallending.breservice.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InvalidRangeException extends RuntimeException{
    public InvalidRangeException(String message) {
        super(message);
    }
}
