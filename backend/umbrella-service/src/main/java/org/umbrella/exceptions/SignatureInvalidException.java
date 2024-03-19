package org.umbrella.exceptions;

public class SignatureInvalidException extends RuntimeException {
    public SignatureInvalidException(String errorMessage) {
        super(errorMessage);
    }
}
