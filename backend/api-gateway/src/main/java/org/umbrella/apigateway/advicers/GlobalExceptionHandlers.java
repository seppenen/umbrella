package org.umbrella.apigateway.advicers;


import org.apache.http.auth.AuthenticationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.reactive.result.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import org.umbrella.apigateway.exceptions.TokenNotFoundException;
import org.umbrella.apigateway.service.LoggerService;

import java.util.Objects;

@RestControllerAdvice
public class GlobalExceptionHandlers extends ResponseEntityExceptionHandler {
    private final LoggerService loggerService;


    public GlobalExceptionHandlers(LoggerService loggerService) {
        this.loggerService = loggerService;

    }

    @ExceptionHandler({
            RuntimeException.class,
            AuthenticationException.class,
            TokenNotFoundException.class
    })
    public ResponseStatusException handleException(ServerWebExchange exchange, Exception ex) {
        return processException(exchange, ex);
    }

    private ResponseStatusException processException(ServerWebExchange exchange, Exception ex) {
        ServerHttpResponse response = setResponseUnauthorized(exchange);
        loggerService.logError(ex, Objects.requireNonNull(response.getStatusCode()));
        return new ResponseStatusException(response.getStatusCode(), ex.getMessage() );
    }

    private ServerHttpResponse setResponseUnauthorized(ServerWebExchange exchange) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        return response;
    }
}

