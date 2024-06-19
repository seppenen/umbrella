package org.umbrella.exceptionHandlers;

/**
 * Author: Aleksandr Seppenen
 * Date: 05-04-2023
 * Version: 1.0
 */


import io.jsonwebtoken.security.SignatureException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import org.umbrella.utils.ApiResponseErrorFactory;

@Order(1)
@ControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandlers extends ResponseEntityExceptionHandler {
    private static final String INTERNAL_ERROR_EXCEPTION = "An internal error has occurred";
    private static final String RESPONSE_INVALID_JSON_EXCEPTION = "Invalid JSON format";

    private static final String UNAUTHORIZED_ACCESS = "Unauthorized access";
    private static final String RESPONSE_ENTITY_NOT_FOUND_ERROR = "Entity not found";
    public static final String RESPONSE_USER_REGISTRATION_FAILED_EXCEPTION = "Failed to register user";

    private final ApiResponseErrorFactory apiErrorFactory;
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandlers.class);




    @ExceptionHandler(RuntimeException.class)
    public void processRuntimeException(RuntimeException ex) {
        logger.info(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @ExceptionHandler({AuthenticationException.class, SignatureException.class})
    public ResponseEntity<ApiErrorResponse> handleAuthenticationException(Exception ex) {
        //TODO: Catch the exception SignatureException
        return handleExceptionLogAndResponse(
                ex,
                UNAUTHORIZED_ACCESS,
                "",
                HttpStatus.UNAUTHORIZED
        );
    }


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


    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleEntityNotFoundException(EntityNotFoundException ex) {
        return handleExceptionLogAndResponse(
                ex,
                RESPONSE_ENTITY_NOT_FOUND_ERROR,
                "Entity is null or not found",
                HttpStatus.NOT_FOUND
        );
    }


    private ResponseEntity<ApiErrorResponse> handleExceptionLogAndResponse(
            Throwable ex,
            String message,
            String detail,
            HttpStatus status
    ) {
        ApiErrorResponse apiErrorResponse = apiErrorFactory.create(status, message, detail);
        logger.info(ex.getMessage(), status);
        return new ResponseEntity<>(apiErrorResponse, new HttpHeaders(), status);
    }
}
