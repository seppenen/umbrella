package org.spring.authservice.exceptionHandlers;

/**
 * Author: Aleksandr Seppenen
 * Date: 05-04-2023
 * Version: 1.0
 */


import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.security.SignatureException;
import org.spring.authservice.service.ApiResponseErrorFactory;
import org.spring.authservice.service.LoggerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;



@ControllerAdvice
public class GlobalExceptionHandlers {

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
    @ExceptionHandler({AuthenticationException.class, SignatureException.class, JwtException.class})
    public Mono<Void> handleAuthenticationException(ServerWebExchange exchange, AuthenticationException ex) {
        //TODO: Catch the exception SignatureException
         ServerHttpResponse response = exchange.getResponse();
         response.setStatusCode(HttpStatus.UNAUTHORIZED);
        return handleExceptionLogAndResponse(response, ex);
    }


    /**
     * Handles the HttpMessageNotReadableException and returns an appropriate API response.
     *
     * @param ex The HttpMessageNotReadableException object representing the exception.
     * @return An instance of ResponseEntity<ApiErrorResponse> that encapsulates the API error response.
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public Mono<Void> httpMessageNotReadableException(ServerWebExchange exchange, AuthenticationException ex) {
        //TODO: Catch the exception SignatureException
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        return handleExceptionLogAndResponse(response, ex);
    }



    /**
     * Handles the exception, logs the error and generates an error response.
     *
     * @param response The server HTTP response.
     * @param ex The authentication exception.
     * @return A Mono representing the completion of the method.
     */
    private Mono<Void> handleExceptionLogAndResponse(ServerHttpResponse response, AuthenticationException ex) {
        loggerService.logError(ex, response.getStatusCode());
        return apiErrorFactory.createErrorResponse(response, null);

    }
}

