package com.service.userService.config;


import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
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
        return Mono.error(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized", ex));
    }
}
