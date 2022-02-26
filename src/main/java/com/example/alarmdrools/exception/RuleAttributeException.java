package com.example.alarmdrools.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public class RuleAttributeException extends RuntimeException{
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public static class NotFound extends BaseException {
        public NotFound(String msg, Object... args) {
            super(msg, args);
        }
    }
}
