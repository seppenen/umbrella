package org.umbrella.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.umbrella.client.AuthServiceClient;
import org.umbrella.exceptions.SignatureInvalidException;
import org.umbrella.service.JwtService;
import org.umbrella.service.LoggerService;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.io.IOException;
import java.util.Arrays;

@Component
public class AuthServiceFilter extends OncePerRequestFilter {

    private final AuthServiceClient authServiceClient;
    private final JwtService jwtService;

    public AuthServiceFilter(AuthServiceClient authServiceClient, JwtService jwtService, LoggerService loggerService) {
        this.authServiceClient = authServiceClient;
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain)
            throws ServletException, IOException {
        String[] ignoredPaths = new String[] {"swagger-ui", "v3/api-docs", "api/v1/refresh-token", "api/v1/health"};
        String path = request.getRequestURI();
        boolean isIgnoredPath = Arrays.stream(ignoredPaths).anyMatch(path::contains);

        if(!isIgnoredPath){
            String bearerToken = jwtService.resolveToken(request);
            validateAndHandleErrors(bearerToken);
        }
        chain.doFilter(request, response);
    }

    private void validateAndHandleErrors(String bearerToken){
        authServiceClient.validateToken(bearerToken)
                .filter(valid -> valid)
                .switchIfEmpty(Mono.error(new SignatureInvalidException("Unauthorized")))
                .publishOn(Schedulers.boundedElastic())
                .block();
    }
}
