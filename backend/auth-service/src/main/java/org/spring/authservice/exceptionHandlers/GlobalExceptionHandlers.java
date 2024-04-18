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

import java.util.Objects;

@RestControllerAdvice
public class GlobalExceptionHandlers extends ResponseEntityExceptionHandler {
    private final LoggerService loggerService;
    private final ApiResponseErrorFactory apiErrorFactory;

    public GlobalExceptionHandlers(LoggerService loggerService, ApiResponseErrorFactory apiErrorFactory) {
        this.loggerService = loggerService;
        this.apiErrorFactory = apiErrorFactory;
    }

    @ExceptionHandler({
            RuntimeException.class,
            AuthenticationException.class,
            SignatureException.class,
            JwtException.class,
            BadCredentialsException.class,
            EntityNotFoundException.class,
            HttpMessageNotReadableException.class
    })
    public Mono<Void> handleException(ServerWebExchange exchange, Exception ex) {
        return processException(exchange, ex);
    }

    private Mono<Void> processException(ServerWebExchange exchange, Exception ex) {
        ServerHttpResponse response = setResponseUnauthorized(exchange);
        loggerService.logError(ex, Objects.requireNonNull(response.getStatusCode()));
        return apiErrorFactory.createErrorResponse(response, ex.getMessage());
    }

    private ServerHttpResponse setResponseUnauthorized(ServerWebExchange exchange) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        return response;
    }
}

