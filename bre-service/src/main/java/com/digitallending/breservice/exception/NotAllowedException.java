package com.digitallending.breservice.exception;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class NotAllowedException extends RuntimeException{
    public NotAllowedException(String message) {
        super(message);
    }
}
