package com.scrum.retrospective.exception;

public class FeedbackItemNotFoundException extends RuntimeException {
    public FeedbackItemNotFoundException(String message) {
        super(message);
    }
}