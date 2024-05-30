package org.spring.authservice.exceptionHandlers;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
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

import java.util.Objects;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandlers extends ResponseEntityExceptionHandler {

    private final LoggerService loggerService;

    @ExceptionHandler({
            RuntimeException.class,
            JwtException.class,
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
        String errorCode = Objects.requireNonNull(setResponseUnauthorized(exchange).getStatusCode()).toString();
        loggerService.getInfoBuilder().withStatusCode(errorCode).withData(ex.getMessage()).log();
        return new ResponseStatusException(response.getStatusCode(), ex.getMessage());
    }

    private ServerHttpResponse setResponseUnauthorized(ServerWebExchange exchange) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        return response;
    }
}

