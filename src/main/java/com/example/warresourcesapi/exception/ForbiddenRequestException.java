package com.example.warresourcesapi.exception;

public class ForbiddenRequestException extends RuntimeException{
    public ForbiddenRequestException(String message) {
        super(message);
    }
}

