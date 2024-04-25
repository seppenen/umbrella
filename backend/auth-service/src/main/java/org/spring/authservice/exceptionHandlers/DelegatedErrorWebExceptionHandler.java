package org.spring.authservice.exceptionHandlers;

import io.jsonwebtoken.JwtException;
import org.spring.authservice.utils.ApiResponseErrorFactory;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;


@ControllerAdvice
public class DelegatedErrorWebExceptionHandler implements ErrorWebExceptionHandler {
    /**
     * Handles a throwable exception by checking if it's an instance of JwtException.
     * If it is, sets the response status code to UNAUTHORIZED and returns an error response.
     * Otherwise, propagates the exception.
     *
     * @param exchange The ServerWebExchange object representing the current server-web exchange.
     * @param ex       The Throwable object representing the exception.
     * @return A Mono<Void> that represents the completion of the handling process.
     */
    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        if (ex instanceof JwtException) {
            ServerHttpResponse response = exchange.getResponse();
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return new ApiResponseErrorFactory().createErrorResponse(exchange.getResponse(), ex.getMessage());
        }
        return Mono.error(ex);
    }
}
