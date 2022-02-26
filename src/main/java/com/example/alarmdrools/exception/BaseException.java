package com.example.alarmdrools.exception;

public class BaseException extends RuntimeException {
    private String message;

    public BaseException() {
    }

    public BaseException(String msg, Object... args) {
        this.message = format(msg, args);
    }

    public String getMessage() {
        return this.message;
    }

    private static String format(String msg, Object... args) {
        msg = msg.replace("{}", "%S");
        msg = msg.replace("[]", "%S");
        return String.format(msg, args);
    }
}

