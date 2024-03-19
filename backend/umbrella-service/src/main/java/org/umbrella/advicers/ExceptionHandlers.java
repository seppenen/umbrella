package org.umbrella.advicers;

/**
 * Author: Aleksandr Seppenen
 * Date: 05-04-2023
 * Version: 1.0
 */


import io.jsonwebtoken.ExpiredJwtException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.umbrella.entity.ApiErrorResponse;
import org.umbrella.service.LoggerService;
import org.umbrella.utils.ApiErrorFactory;


@ControllerAdvice
public class ExceptionHandlers {
    private static final String INTERNAL_ERROR_EXCEPTION = "An internal error has occurred";
    private static final String RESPONSE_INVALID_JSON_EXCEPTION = "Invalid JSON format";

    private static final String RESPONSE_ENTITY_NOT_FOUND_ERROR = "Entity not found";

    public static final String RESPONSE_USER_REGISTRATION_FAILED_EXCEPTION = "Failed to register user";

    private final LoggerService loggerService;
    private final ApiErrorFactory apiErrorFactory;

    @Autowired
    public ExceptionHandlers(LoggerService loggerService, ApiErrorFactory apiErrorFactory) {
        this.loggerService = loggerService;

        this.apiErrorFactory = apiErrorFactory;
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public void processRuntimeException(ExpiredJwtException ex) {
        loggerService.logError(ex, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(RuntimeException.class)
    public void processRuntimeException(RuntimeException ex) {
        loggerService.logError(ex, HttpStatus.INTERNAL_SERVER_ERROR);
    }


    /**
     * Handles the HttpMessageNotReadableException and returns an appropriate API response.
     *
     * @param ex The HttpMessageNotReadableException object representing the exception.
     * @return An instance of ResponseEntity<ApiErrorResponse> that encapsulates the API error response.
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiErrorResponse> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        return handleExceptionAndResponse(
                ex,
                RESPONSE_INVALID_JSON_EXCEPTION,
                "The provided JSON is not valid",
                HttpStatus.BAD_REQUEST
        );
    }


    /**
     * Handles an EntityNotFoundException and returns an appropriate API response.
     *
     * @param ex The EntityNotFoundException object representing the exception.
     * @return An instance of ResponseEntity<ApiErrorResponse> that encapsulates the API error response.
     */
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleEntityNotFoundException(EntityNotFoundException ex) {
        return handleExceptionAndResponse(
                ex,
                RESPONSE_ENTITY_NOT_FOUND_ERROR,
                "Entity is null or not found",
                HttpStatus.NOT_FOUND
        );
    }


    /**
     * Handles an exception and returns an appropriate API response.
     *
     * @param ex      The Throwable object representing the exception.
     * @param message The custom message to be included in the API error response.
     * @param detail  The detailed description of the error to be included in the API error response.
     * @param status  The HttpStatus code to be included in the API error response.
     * @return An instance of ResponseEntity<ApiErrorResponse> that encapsulates the API error response.
     */
    private ResponseEntity<ApiErrorResponse> handleExceptionAndResponse(
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

