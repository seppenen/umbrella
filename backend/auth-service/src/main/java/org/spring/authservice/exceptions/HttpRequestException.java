package org.spring.authservice.exceptions;

import jakarta.persistence.EntityNotFoundException;
import lombok.Getter;
import org.springframework.security.authentication.BadCredentialsException;

@Getter
public class HttpRequestException extends RuntimeException {
    private static final int UNAUTHORIZED_ERROR_CODE = 401;
    private static final int NOT_FOUND_ERROR_CODE = 404;


    public void throwExceptionForErrorCode(int errorCode,String message) {
        if (errorCode == UNAUTHORIZED_ERROR_CODE) {
            throw new BadCredentialsException(message, this);
        } else if (errorCode == NOT_FOUND_ERROR_CODE) {
            throw new EntityNotFoundException(message);
        }
    }

    public HttpRequestException(int errorCode, String message) {
        super(message);
        throwExceptionForErrorCode(errorCode, message);
    }
}


