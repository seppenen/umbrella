package com.service.userService.exceptions;


import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;

public class HttpRequestException extends RuntimeException {

    public HttpRequestException(HttpStatus httpStatus, String message) {
        super(message);
        checkHttpStatus(httpStatus, message);
    }

    private void checkHttpStatus(HttpStatus httpStatus, String message) {
        if (httpStatus == HttpStatus.UNAUTHORIZED) {
            throw new BadCredentialsException(message);
        }
        if (httpStatus == HttpStatus.NOT_FOUND) {
            throw new EntityNotFoundException(message);
        }
    }
}


