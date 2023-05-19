package com.guidepedia.exception;

public class AuthorizationException extends RuntimeException{
    public AuthorizationException(String message) {
        super(message);
    }

}