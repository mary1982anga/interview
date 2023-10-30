package com.scrum.retrospective.exception;

public class RetrospectiveNotFoundException extends RuntimeException {
    public RetrospectiveNotFoundException(String message) {
        super(message);
    }
}