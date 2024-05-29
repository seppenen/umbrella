package org.spring.authservice.exceptionHandlers;

import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.persistence.EntityNotFoundException;
import org.spring.authservice.service.LoggerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.reactive.result.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;

@RestControllerAdvice
public class GlobalExceptionHandlers extends ResponseEntityExceptionHandler {
    private final LoggerService loggerService;


    public GlobalExceptionHandlers(LoggerService loggerService) {
        this.loggerService = loggerService;

    }

    @ExceptionHandler({
            RuntimeException.class,
            AuthenticationException.class,
            SignatureException.class,
            MalformedJwtException.class,
            BadCredentialsException.class,
            EntityNotFoundException.class,
            HttpMessageNotReadableException.class
    })
    public ResponseStatusException handleException(ServerWebExchange exchange, Exception ex) {
        return processException(exchange, ex);
    }

    private ResponseStatusException processException(ServerWebExchange exchange, Exception ex) {
        ServerHttpResponse response = setResponseUnauthorized(exchange);
        loggerService.getInfoBuilder().withStatusCode(response.getStatusCode()).withData(ex.getMessage()).log();
        return new ResponseStatusException(response.getStatusCode(), ex.getMessage());
    }

    private ServerHttpResponse setResponseUnauthorized(ServerWebExchange exchange) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        return response;
    }
}

