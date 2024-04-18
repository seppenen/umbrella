package com.service.userService.advicers;

import com.service.userService.entity.ApiErrorResponse;
import com.service.userService.exceptions.UserRegistrationFailedException;
import com.service.userService.service.LoggerService;
import com.service.userService.utils.ApiErrorFactory;
import jakarta.persistence.EntityNotFoundException;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@ControllerAdvice
public class ExceptionControllerAdvice {
    private final LoggerService loggerService;
    private final ApiErrorFactory apiErrorFactory;

    @Autowired
    public ExceptionControllerAdvice(LoggerService loggerService, ApiErrorFactory apiErrorFactory) {
        this.loggerService = loggerService;
        this.apiErrorFactory = apiErrorFactory;
    }

    @ExceptionHandler(Exception.class)
    public void handleGenericException(Exception ex) {
        loggerService.logError(ex, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiErrorResponse> handleNotReadableException(HttpMessageNotReadableException ex) {
        return useApiErrorResponse(ex, ErrorMessage.RESPONSE_INVALID_JSON_EXCEPTION.getMsg(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserRegistrationFailedException.class)
    public ResponseEntity<ApiErrorResponse> handleRegistrationFailedException(UserRegistrationFailedException ex) {
        return useApiErrorResponse(ex, ErrorMessage.RESPONSE_USER_REGISTRATION_FAILED_EXCEPTION.getMsg(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiErrorResponse> handleBadCredentialsException(BadCredentialsException ex) {
        return useApiErrorResponse(ex, ErrorMessage.RESPONSE_USER_LOGIN_FAILED_EXCEPTION.getMsg(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleEntityNotFoundException(EntityNotFoundException ex) {
        return useApiErrorResponse(ex, ErrorMessage.RESPONSE_ENTITY_NOT_FOUND_ERROR.getMsg(), HttpStatus.UNAUTHORIZED);
    }

    private ResponseEntity<ApiErrorResponse> useApiErrorResponse(Throwable ex, String message, HttpStatus status) {
        ApiErrorResponse apiErrorResponse = apiErrorFactory.create(status, message, ex.getMessage());
        loggerService.logError(ex, status);
        return new ResponseEntity<>(apiErrorResponse, null, status);
    }

    @Getter
    enum ErrorMessage {
        RESPONSE_INVALID_JSON_EXCEPTION("Invalid JSON format"),
        RESPONSE_USER_REGISTRATION_FAILED_EXCEPTION("Failed to register user"),
        RESPONSE_USER_LOGIN_FAILED_EXCEPTION("Login or password is incorrect"),
        RESPONSE_ENTITY_NOT_FOUND_ERROR("Entity not found");

        private final String msg;

        ErrorMessage(String msg) {
            this.msg = msg;
        }

    }
}

