package org.spring.authservice.exceptionHandlers;

import org.spring.authservice.utils.ApiResponseErrorFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * DelegatedAuthenticationEntryPoint class is an implementation of the AuthenticationEntryPoint interface.
 * It provides a way to handle authentication failures in a delegated manner by using a HandlerExceptionResolver to resolve the exception and return an appropriate response.
 */
@Component
public class DelegatedServerAuthenticationEntryPoint implements ServerAuthenticationEntryPoint  {
    @Override
    public Mono<Void> commence(ServerWebExchange exchange, AuthenticationException ex) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        return new ApiResponseErrorFactory().createErrorResponse(response, ex.getMessage());
    }
}
