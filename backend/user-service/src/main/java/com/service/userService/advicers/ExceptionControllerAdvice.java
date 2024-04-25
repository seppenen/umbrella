package com.service.userService.advicers;

import com.service.userService.exceptions.UserRegistrationFailedException;
import com.service.userService.service.LoggerService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;


/**
 * The ExceptionControllerAdvice class is a controller advice class that handles exceptions thrown by the application.
 * It provides handler methods for different types of exceptions and logs the error using the LoggerService class.
 */
@ControllerAdvice
@AllArgsConstructor
public class ExceptionControllerAdvice {

    private final LoggerService loggerService;


    @ExceptionHandler(Exception.class)
    public void handleGenericException(Exception ex) {
        loggerService.logError(ex, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseStatusException handleNotReadableException(HttpMessageNotReadableException ex) {
        return useApiErrorResponse(ex, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserRegistrationFailedException.class)
    public ResponseStatusException handleRegistrationFailedException(UserRegistrationFailedException ex) {
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
        return new ResponseStatusException(status, status.getReasonPhrase());
    }


}

