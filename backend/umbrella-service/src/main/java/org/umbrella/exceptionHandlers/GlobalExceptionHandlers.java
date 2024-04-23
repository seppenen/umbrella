package org.umbrella.exceptionHandlers;

/**
 * Author: Aleksandr Seppenen
 * Date: 05-04-2023
 * Version: 1.0
 */


import io.jsonwebtoken.security.SignatureException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.umbrella.entity.ApiErrorResponse;
import org.umbrella.service.ApiResponseErrorFactory;
import org.umbrella.service.LoggerService;

@ControllerAdvice
public class GlobalExceptionHandlers extends ResponseEntityExceptionHandler {

    private final LoggerService loggerService;
    private final ApiResponseErrorFactory apiErrorFactory;

    public GlobalExceptionHandlers(LoggerService loggerService, ApiResponseErrorFactory apiErrorFactory) {
        this.loggerService = loggerService;
        this.apiErrorFactory = apiErrorFactory;
    }

    @ExceptionHandler({
            AuthenticationException.class,
            SignatureException.class,
            EntityNotFoundException.class,
            HttpMessageNotReadableException.class
    })
    public ResponseEntity<ApiErrorResponse> handleException(Exception ex) {
        if (ex instanceof AuthenticationException || ex instanceof SignatureException) {
            return handleExceptionLogAndResponse(ex, "Unauthorized access", "", HttpStatus.UNAUTHORIZED);
        } else if (ex instanceof HttpMessageNotReadableException) {
            return handleExceptionLogAndResponse(ex, "Invalid JSON format", "The provided JSON is not valid", HttpStatus.BAD_REQUEST);
        } else if (ex instanceof EntityNotFoundException) {
            return handleExceptionLogAndResponse(ex, "Entity not found", "Entity is null or not found", HttpStatus.UNAUTHORIZED);
        } else {
            // handle general exceptions
            return handleExceptionLogAndResponse(ex, "An internal error has occurred", "", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private ResponseEntity<ApiErrorResponse> handleExceptionLogAndResponse(
            Throwable ex,
            String message,
            String detail,
            HttpStatus status
    ) {
        ApiErrorResponse apiErrorResponse = apiErrorFactory.create(status, message, detail);
        loggerService.logError(ex, status);
        return new ResponseEntity<>(apiErrorResponse, new HttpHeaders(), status);
    }
}
