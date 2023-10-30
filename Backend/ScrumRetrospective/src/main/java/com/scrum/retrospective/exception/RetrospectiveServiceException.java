package com.scrum.retrospective.exception;

public class RetrospectiveServiceException extends RuntimeException {
    
	public RetrospectiveServiceException(String message) {
        super(message);
    }

    public RetrospectiveServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}