package com.example.alarmdrools.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public class WorkOrderException extends Exception{
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public static class ValidationFailure extends BaseException {
        public ValidationFailure(String msg, Object... args) {
            super(msg, args);
        }
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    public static class ItemNotFound extends BaseException {
        public ItemNotFound(String msg, Object... args) {
            super(msg, args);
        }
    }

    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public static class AccessDenied extends BaseException {
        public AccessDenied(String msg, Object... args) {
            super(msg, args);
        }
    }
}
