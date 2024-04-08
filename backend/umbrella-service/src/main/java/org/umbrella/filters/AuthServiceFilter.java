package org.umbrella.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.umbrella.client.AuthServiceClient;
import reactor.core.publisher.Mono;

import java.io.IOException;

@Component
public class AuthServiceFilter extends OncePerRequestFilter {

    private final AuthServiceClient authServiceClient;

    public AuthServiceFilter(AuthServiceClient authServiceClient) {
        this.authServiceClient = authServiceClient;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain)
            throws ServletException, IOException {
        String bearerToken = extractToken(request);
        validateAndHandleErrors(bearerToken, response);
        chain.doFilter(request, response);
    }

    private void validateAndHandleErrors(String bearerToken, HttpServletResponse response){
        authServiceClient.validateToken(bearerToken)
                .filter(valid -> valid)
                .switchIfEmpty(Mono.error(new BadCredentialsException("Unauthorized")))
                .doOnError(e -> {
                    try {
                        response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                }).block();
    }

    private String extractToken(HttpServletRequest request) {
        // To be implemented: Extract token from the request headers
        return null;
    }
}
