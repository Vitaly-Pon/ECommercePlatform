package com.vitaliy.authservice.exeption;

public class EmailAlreadyExistsException extends  RuntimeException {
    public EmailAlreadyExistsException(String message) {
        super(message);
    }
}
