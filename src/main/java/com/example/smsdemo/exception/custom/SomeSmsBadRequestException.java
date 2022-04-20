package com.example.smsdemo.exception.custom;

public class SomeSmsBadRequestException extends RuntimeException {

    private static final long serialVersionUID = 6526852701000440675L;

    public SomeSmsBadRequestException(String message) {
        super(message);
    }
}
