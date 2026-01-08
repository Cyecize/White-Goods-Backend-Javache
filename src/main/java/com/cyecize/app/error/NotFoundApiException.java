package com.cyecize.app.error;

public class NotFoundApiException extends RuntimeException {

    public NotFoundApiException(String message) {
        super(message);
    }
}
