package org.umbrella.apigateway.filters;

import org.apache.http.auth.AuthenticationException;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.umbrella.apigateway.client.AuthServiceClient;
import org.umbrella.apigateway.exceptions.TokenNotFoundException;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.Optional;

@Component
public class AuthorizationFilter implements GlobalFilter {

    private static final String[] ALLOWED_PATHS = {
            "/api/v1/health",
            "/api/v1/authenticate",
            "/swagger-ui",
            "/v3/api-docs"
    };
    private static final String BEARER_PREFIX = "Bearer ";

    private final AuthServiceClient authServiceClient;

    public AuthorizationFilter(AuthServiceClient authServiceClient) {
        this.authServiceClient = authServiceClient;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        if (isAllowedPath(exchange)) {
            Optional<String> token = extractToken(exchange);
            if (token.isPresent()) {
                return validateAndHandleErrors(token.get(), chain, exchange);
            } else {
                throw new TokenNotFoundException("Token not found");
            }
        }
        return chain.filter(exchange);
    }

    private boolean isAllowedPath(ServerWebExchange exchange) {
        String path = exchange.getRequest().getURI().getPath();
        return Arrays.stream(ALLOWED_PATHS).noneMatch(path::contains);
    }



    private Mono<Void> validateAndHandleErrors(String token, GatewayFilterChain chain, ServerWebExchange exchange) {
        return authServiceClient.authorize(token)
                .filter(Boolean::booleanValue)
                .switchIfEmpty(Mono.error(new AuthenticationException("Unauthorized")))
                .then(chain.filter(exchange));
    }

    private Optional<String> extractToken(ServerWebExchange exchange) {
        return Optional.ofNullable(exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION))
                .filter(authHeader -> authHeader.startsWith(BEARER_PREFIX))
                .map(authHeader -> authHeader.substring(BEARER_PREFIX.length()));
    }
}
