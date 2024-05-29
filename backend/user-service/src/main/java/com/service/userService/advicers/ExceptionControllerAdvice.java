package com.service.userService.advicers;

import com.service.userService.exceptions.EntityPersistenceException;
import com.service.userService.service.impl.LoggerServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.reactive.result.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.server.ResponseStatusException;


/**
 * The ExceptionControllerAdvice class is a controller advice class that handles exceptions thrown by the application.
 * It provides handler methods for different types of exceptions and logs the error using the LoggerService class.
 */
@ControllerAdvice
@AllArgsConstructor
public class ExceptionControllerAdvice extends ResponseEntityExceptionHandler  {

    private final LoggerServiceImpl loggerService;


    @ExceptionHandler(Exception.class)
    public void handleGenericException(Exception ex) {
        loggerService.logError(ex, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseStatusException handleAuthenticationException(AuthenticationException ex) {
        return useApiErrorResponse(ex, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseStatusException handleNotReadableException(HttpMessageNotReadableException ex) {
        return useApiErrorResponse(ex, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EntityPersistenceException.class)
    public ResponseStatusException handleRegistrationFailedException(EntityPersistenceException ex) {
        return useApiErrorResponse(ex, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseStatusException handleBadCredentialsException(BadCredentialsException ex) {
        return useApiErrorResponse(ex, HttpStatus.UNAUTHORIZED);
    }


    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseStatusException handleEntityNotFoundException(EntityNotFoundException ex) {
        return useApiErrorResponse(ex, HttpStatus.NOT_FOUND);
    }

    private ResponseStatusException useApiErrorResponse(Throwable ex, HttpStatus status) {
        loggerService.logError(ex, status);
        return new ResponseStatusException(status, status.getReasonPhrase(), ex);
    }


}

