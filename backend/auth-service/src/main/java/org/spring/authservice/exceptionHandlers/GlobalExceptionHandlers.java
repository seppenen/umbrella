package org.spring.authservice.exceptionHandlers;

/**
 * Author: Aleksandr Seppenen
 * Date: 05-04-2023
 * Version: 1.0
 */


import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.security.SignatureException;
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


/**
 * GlobalExceptionHandlers is a class responsible for handling global exceptions in the application.
 * It extends the ResponseEntityExceptionHandler class and is annotated with @RestControllerAdvice.
 * It provides error handling for various exceptions and logs the errors using the LoggerService class.
 */
@RestControllerAdvice
public class GlobalExceptionHandlers extends ResponseEntityExceptionHandler {

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


    @ExceptionHandler({AuthenticationException.class, SignatureException.class, JwtException.class})
    public Mono<Void> handleAuthenticationException(ServerWebExchange exchange, AuthenticationException ex) {
        //TODO: Catch the exception SignatureException
         ServerHttpResponse response = exchange.getResponse();
         response.setStatusCode(HttpStatus.UNAUTHORIZED);
        return handleExceptionLogAndResponse(response, ex);
    }

    @ExceptionHandler({BadCredentialsException.class})
    public Mono<Void> handleBadCredentials(ServerWebExchange exchange, BadCredentialsException ex) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        return handleExceptionLogAndResponse(response, ex);
    }


    @ExceptionHandler(HttpMessageNotReadableException.class)
    public Mono<Void> httpMessageNotReadableException(ServerWebExchange exchange, AuthenticationException ex) {
        //TODO: Catch the exception SignatureException
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        return handleExceptionLogAndResponse(response, ex);
    }



    private Mono<Void> handleExceptionLogAndResponse(ServerHttpResponse response, Exception ex) {
        loggerService.logError(ex, response.getStatusCode());
        return apiErrorFactory.createErrorResponse(response, ex.getMessage());

    }
}

