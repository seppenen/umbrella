package org.spring.authservice.filters;

import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.Payload;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.spring.authservice.service.AuthService;
import org.spring.authservice.service.JwtService;
import org.spring.authservice.service.LoggerService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;


//TODO: Consider deleting this class

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter implements WebFilter {
    private final JwtService jwtService;
    private final AuthService authService;
    private final LoggerService loggerService;

    private static final String[] ALLOWED_PATHS = {
            "/api/v1/health",
            "/api/v1/authenticate",
            "/swagger-ui",
            "/v3/api-docs"
    };

    private UsernamePasswordAuthenticationToken handleAuthenticationToken(Map<String, Claim> claimMap, String token) {
        String principal = claimMap.get("principal").asString();
        return new UsernamePasswordAuthenticationToken(principal, token, new ArrayList<>());

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
        if (token != null) {
             return jwtService.validateToken(token)
                     .map(Payload::getClaims)
                     .mapNotNull(claimMap -> handleAuthenticationToken(claimMap, token))
                     .flatMap(auth -> chain.filter(exchange)
                            .contextWrite(ReactiveSecurityContextHolder.withAuthentication(auth)));
            }
        loggerService.getErrorBuilder().withStatusCode("401").withMessage("Token not presented").log();
        return chain.filter(exchange);
    }
}
