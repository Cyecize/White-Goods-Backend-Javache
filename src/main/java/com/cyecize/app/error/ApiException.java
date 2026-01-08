package com.cyecize.app.error;

public class ApiException extends RuntimeException {

    public ApiException(String message) {
        super(message);
    }
}
