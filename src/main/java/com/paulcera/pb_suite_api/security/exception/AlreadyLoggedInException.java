package com.paulcera.pb_suite_api.security.exception;

public class AlreadyLoggedInException extends RuntimeException {

    public AlreadyLoggedInException(String message) {
        super(message);
    }

}
