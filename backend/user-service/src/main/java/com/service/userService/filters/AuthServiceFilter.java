package com.service.userService.filters;

import com.service.userService.client.AuthServiceClient;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Arrays;

@Component
public class AuthServiceFilter implements WebFilter {

   private final AuthServiceClient authServiceClient;
    private static final String BEARER_PREFIX = "Bearer ";

    private static final String[] ALLOWED_PATHS = {
            "/api/v1/health",
            "/api/v1/login",
            "/api/v1/register",
            "/swagger-ui",
            "/v3/api-docs"
    };

    public AuthServiceFilter(AuthServiceClient authServiceClient) {
        this.authServiceClient = authServiceClient;
    }

    @Override
    public @NotNull Mono<Void> filter(@NotNull ServerWebExchange exchange, WebFilterChain chain) {

        String path = exchange.getRequest().getURI().getPath();
        boolean isIgnoredPath = Arrays.stream(ALLOWED_PATHS).anyMatch(path::contains);
        if (isIgnoredPath) {
            return chain.filter(exchange);
        }
        String token = extractToken(exchange);
        return validateAndSetSecurityContext(token, chain, exchange);
    }

    private String extractToken(ServerWebExchange exchange) {
        String bearerToken = exchange.getRequest().getHeaders().getFirst("Authorization");
        if (bearerToken != null && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(7);
        }
        return null;
    }


    private Mono<Void> validateAndSetSecurityContext(String token,  WebFilterChain chain, ServerWebExchange exchange) {
        return authServiceClient.authorize(token)
                .filter(Boolean::booleanValue)
                .switchIfEmpty(Mono.error(new AuthenticationException("Unauthorized request") {}))
                .flatMap(isAuthorized -> {
                    Authentication auth = new UsernamePasswordAuthenticationToken(token, token, new ArrayList<>());
                    return chain.filter(exchange)
                            .contextWrite(ReactiveSecurityContextHolder.withAuthentication(auth));
                })
                .onErrorResume(AuthenticationException.class, e -> Mono.error(new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED, "Unauthorized request", e)));
    }
}
