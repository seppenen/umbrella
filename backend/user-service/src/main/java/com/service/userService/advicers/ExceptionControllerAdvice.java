package com.service.userService.advicers;

import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.reactive.result.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;

import java.util.Objects;


/**
 * The ExceptionControllerAdvice class is a controller advice class that handles exceptions thrown by the application.
 * It provides handler methods for different types of exceptions and logs the error using the LoggerService class.
 */
@ControllerAdvice
@AllArgsConstructor
public class ExceptionControllerAdvice extends ResponseEntityExceptionHandler  {

    private static final Logger logger = LogManager.getLogger(ExceptionControllerAdvice.class);

    @ExceptionHandler(Exception.class)
    public ResponseStatusException handleException(ServerWebExchange exchange, Exception ex) {
        return processException(exchange, ex);
    }

    private ResponseStatusException processException(ServerWebExchange exchange, Exception ex) {
        ServerHttpResponse response;
        if (!(ex instanceof BadCredentialsException) && !(ex instanceof UsernameNotFoundException)) {
            response = setResponseStatus(exchange, HttpStatus.INTERNAL_SERVER_ERROR);
        } else {
            response = setResponseStatus(exchange, HttpStatus.UNAUTHORIZED);
        }
        String errorCode = Objects.requireNonNull(response).toString();
        logger.error("Error code: " + errorCode + " - Error message: " + ex.getMessage(), ex);
        return new ResponseStatusException(response.getStatusCode(), ex.getMessage());
    }

    private ServerHttpResponse setResponseStatus(ServerWebExchange exchange, HttpStatus httpStatus) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        return response;
    }
}

