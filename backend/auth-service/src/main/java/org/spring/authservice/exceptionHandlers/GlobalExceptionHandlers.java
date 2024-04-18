package org.spring.authservice.exceptionHandlers;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.persistence.EntityNotFoundException;
import org.apache.http.auth.AuthenticationException;
import org.spring.authservice.service.ApiResponseErrorFactory;
import org.spring.authservice.service.LoggerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.reactive.result.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@RestControllerAdvice
public class GlobalExceptionHandlers extends ResponseEntityExceptionHandler {
    private final LoggerService loggerService;
    private final ApiResponseErrorFactory apiErrorFactory;

    public GlobalExceptionHandlers(LoggerService loggerService, ApiResponseErrorFactory apiErrorFactory) {
        this.loggerService = loggerService;
        this.apiErrorFactory = apiErrorFactory;
    }

    @ExceptionHandler(RuntimeException.class)
    public void logRuntimeException(RuntimeException ex) {
        loggerService.logError(ex, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({AuthenticationException.class, SignatureException.class, JwtException.class})
    public Mono<Void> handleAuthenticationException(ServerWebExchange exchange, AuthenticationException ex) {
        return processException(exchange, ex);
    }

    @ExceptionHandler({BadCredentialsException.class})
    public Mono<Void> handleBadCredentials(ServerWebExchange exchange, BadCredentialsException ex) {
        return processException(exchange, ex);
    }

    @ExceptionHandler({EntityNotFoundException.class})
    public Mono<Void> handleEntityNotFoundException(ServerWebExchange exchange, EntityNotFoundException ex) {
        return processException(exchange, ex);
    }


    @ExceptionHandler(HttpMessageNotReadableException.class)
    public Mono<Void> handleHttpMessageNotReadableException(ServerWebExchange exchange, HttpMessageNotReadableException ex) {
        return processException(exchange, ex);
    }

    private Mono<Void> processException(ServerWebExchange exchange, Exception ex) {
        ServerHttpResponse response = setResponseUnauthorized(exchange);
        loggerService.logError(ex, response.getStatusCode());
        return apiErrorFactory.createErrorResponse(response, ex.getMessage());
    }

    private ServerHttpResponse setResponseUnauthorized(ServerWebExchange exchange) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        return response;
    }
}

