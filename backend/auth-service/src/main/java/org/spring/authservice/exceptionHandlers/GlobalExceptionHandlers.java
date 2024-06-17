package org.spring.authservice.exceptionHandlers;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.reactive.result.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;

import java.util.Objects;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandlers extends ResponseEntityExceptionHandler {
    private static final Logger logger = LogManager.getLogger(GlobalExceptionHandlers.class);

    @ExceptionHandler(Exception.class)
    public ResponseStatusException handleException(ServerWebExchange exchange, Exception ex) {
        return processException(exchange, ex);
    }

    private ResponseStatusException processException(ServerWebExchange exchange, Exception ex) {
        ServerHttpResponse response;
        if (ex instanceof RuntimeException) {
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

