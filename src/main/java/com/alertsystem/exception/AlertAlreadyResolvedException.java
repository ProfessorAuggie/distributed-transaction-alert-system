package com.alertsystem.exception;

public class AlertAlreadyResolvedException extends RuntimeException {
    private final String errorCode;

    public AlertAlreadyResolvedException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
