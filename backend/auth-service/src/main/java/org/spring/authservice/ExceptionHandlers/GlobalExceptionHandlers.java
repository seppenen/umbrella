package org.spring.authservice.ExceptionHandlers;

/**
 * Author: Aleksandr Seppenen
 * Date: 05-04-2023
 * Version: 1.0
 */


import entity.ApiErrorResponse;
import io.jsonwebtoken.security.SignatureException;
import org.spring.authservice.service.ApiResponseErrorFactory;
import org.spring.authservice.service.LoggerService;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


@Order(1)
@ControllerAdvice
public class GlobalExceptionHandlers extends ResponseEntityExceptionHandler {
    private static final String INTERNAL_ERROR_EXCEPTION = "An internal error has occurred";

    private static final String UNAUTHORIZED_ACCESS = "Unauthorized access";
    private final LoggerService loggerService;
    private final ApiResponseErrorFactory apiErrorFactory;


    public GlobalExceptionHandlers(LoggerService loggerService, ApiResponseErrorFactory apiErrorFactory) {
        this.loggerService = loggerService;
        this.apiErrorFactory = apiErrorFactory;
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiErrorResponse> processRuntimeException(RuntimeException ex) {
        return handleExceptionLogAndResponse(
                ex,
                INTERNAL_ERROR_EXCEPTION,
                "",
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }


    /**
     * Handles AuthenticationException and SignatureException and returns an appropriate API response.
     *
     * @param ex The Exception object representing the exception.
     * @return An instance of ResponseEntity<ApiErrorResponse> that encapsulates the API error response.
     */
    @ExceptionHandler({AuthenticationException.class, SignatureException.class})
    public ResponseEntity<ApiErrorResponse> handleAuthenticationException(Exception ex) {
        return handleExceptionLogAndResponse(
                ex,
                UNAUTHORIZED_ACCESS,
                "",
                HttpStatus.UNAUTHORIZED
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

