package org.umbrella.apigateway.advicers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.reactive.result.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import org.umbrella.apigateway.service.LoggerService;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandlers extends ResponseEntityExceptionHandler {

    private final LoggerService loggerService;

    @ExceptionHandler({
            RuntimeException.class,
            AuthenticationException.class
    })
    public ResponseStatusException handleException(ServerWebExchange exchange, Exception ex) {
        return processException(exchange, ex);
    }

    private ResponseStatusException processException(ServerWebExchange exchange, Exception ex) {
        ServerHttpResponse response = setResponseStatusCode(exchange,ex);

        loggerService.logError(ex, response.getStatusCode());
        return new ResponseStatusException(response.getStatusCode(), ex.getMessage());
    }

    private ServerHttpResponse setResponseStatusCode(ServerWebExchange exchange, Exception ex) {
        ServerHttpResponse response = exchange.getResponse();
        if( ex instanceof AuthenticationException) {
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
        }  else if(ex instanceof RuntimeException) {
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
        } else {
            // default status code
            response.setStatusCode(HttpStatus.BAD_REQUEST);
        }
        return response;
    }
}

