package org.umbrella.exceptionHandlers;

/**
 * Author: Aleksandr Seppenen
 * Date: 05-04-2023
 * Version: 1.0
 */


import io.jsonwebtoken.security.SignatureException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.umbrella.entity.ApiErrorResponse;
import org.umbrella.service.ApiResponseErrorFactory;
import org.umbrella.service.LoggerService;

@Order(1)
@ControllerAdvice
public class GlobalExceptionHandlers extends ResponseEntityExceptionHandler {
    private static final String INTERNAL_ERROR_EXCEPTION = "An internal error has occurred";
    private static final String RESPONSE_INVALID_JSON_EXCEPTION = "Invalid JSON format";

    private static String INVALID_JWT_TOKEN = "Jwt token invalid";
    private static final String RESPONSE_ENTITY_NOT_FOUND_ERROR = "Entity not found";
    public static final String RESPONSE_USER_REGISTRATION_FAILED_EXCEPTION = "Failed to register user";

    private final LoggerService loggerService;
    private final ApiResponseErrorFactory apiErrorFactory;


    public GlobalExceptionHandlers(LoggerService loggerService, ApiResponseErrorFactory apiErrorFactory) {
        this.loggerService = loggerService;

        this.apiErrorFactory = apiErrorFactory;
    }

    @ExceptionHandler(RuntimeException.class)
    public void processRuntimeException(RuntimeException ex) {
        loggerService.logError(ex, HttpStatus.INTERNAL_SERVER_ERROR);
    }



    /**
     * Handles AuthenticationException and SignatureException and returns an appropriate API response.
     *
     * @param ex The Exception object representing the exception.
     * @return An instance of ResponseEntity<ApiErrorResponse> that encapsulates the API error response.
     */
    @ExceptionHandler({AuthenticationException.class, SignatureException.class})
    public ResponseEntity<ApiErrorResponse> handleAuthenticationException(Exception ex) {
        //TODO: Catch the exception SignatureException
        return handleExceptionLogAndResponse(
                ex,
                INVALID_JWT_TOKEN,
                "The provided JWT token is not valid or has expired",
                HttpStatus.UNAUTHORIZED
        );
    }


    /**
     * Handles the HttpMessageNotReadableException and returns an appropriate API response.
     *
     * @param ex The HttpMessageNotReadableException object representing the exception.
     * @return An instance of ResponseEntity<ApiErrorResponse> that encapsulates the API error response.
     */
    @Override
    public ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        ResponseEntity<ApiErrorResponse> responseEntity = handleExceptionLogAndResponse(
                ex,
                RESPONSE_INVALID_JSON_EXCEPTION,
                "The provided JSON is not valid",
                HttpStatus.BAD_REQUEST
        );
        return new ResponseEntity<>(responseEntity.getBody(), responseEntity.getStatusCode());
    }


    /**
     * Handles an EntityNotFoundException and returns an appropriate API response.
     *
     * @param ex The EntityNotFoundException object representing the exception.
     * @return An instance of ResponseEntity<ApiErrorResponse> that encapsulates the API error response.
     */
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleEntityNotFoundException(EntityNotFoundException ex) {
        return handleExceptionLogAndResponse(
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

