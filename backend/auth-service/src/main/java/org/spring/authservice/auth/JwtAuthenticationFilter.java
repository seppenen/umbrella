package org.spring.authservice.auth;

import com.auth0.jwt.interfaces.Payload;
import org.jetbrains.annotations.NotNull;
import org.spring.authservice.service.AuthService;
import org.spring.authservice.service.JwtService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Arrays;

@Component
public class JwtAuthenticationFilter implements WebFilter {
    private final JwtService jwtService;
    private final AuthService authService;

    private static final String[] ALLOWED_PATHS = {
            "/api/v1/health",
            "/api/v1/authenticate",
            "/swagger-ui",
            "/v3/api-docs"
    };
    public JwtAuthenticationFilter(JwtService jwtService, AuthService authService) {
        this.jwtService = jwtService;
        this.authService = authService;
    }

    @Override
    public @NotNull Mono<Void> filter(@NotNull ServerWebExchange exchange, WebFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();
        boolean isIgnoredPath = Arrays.stream(ALLOWED_PATHS).anyMatch(path::contains);
        if (isIgnoredPath) {
            return chain.filter(exchange);
        }
        String token = jwtService.extractToken(exchange);
        boolean isTokenPresent = authService.findRefreshToken(token).isPresent();
        if (token != null && isTokenPresent) {
             return jwtService.validateToken(token).map(Payload::getIssuer)
                    .map(iss -> new UsernamePasswordAuthenticationToken(iss, token, new ArrayList<>()))
                    .flatMap(auth -> chain.filter(exchange)
                            .contextWrite(ReactiveSecurityContextHolder.withAuthentication(auth)));
            }
        return chain.filter(exchange);
    }
}
