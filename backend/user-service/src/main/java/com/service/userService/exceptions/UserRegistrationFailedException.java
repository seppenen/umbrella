package com.service.userService.exceptions;

public class UserRegistrationFailedException extends RuntimeException {
    public UserRegistrationFailedException(Throwable cause) {
        super(cause);
    }
}
