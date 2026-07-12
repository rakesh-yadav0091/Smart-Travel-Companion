package com.travelcompanion.exception;

/**
 * Custom exception class for travel-related errors.
 * Demonstrates custom exception handling.
 *
 * @author CSY2094 Student
 */
public class TravelException extends Exception {

    private String errorCode;

    public TravelException(String message) {
        super(message);
    }

    public TravelException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public TravelException(String message, Throwable cause) {
        super(message, cause);
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    @Override
    public String toString() {
        if (errorCode != null) {
            return "TravelException [Error Code: " + errorCode + "] " + getMessage();
        }
        return "TravelException: " + getMessage();
    }
}
