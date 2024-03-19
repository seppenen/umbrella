package org.umbrella.utils;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.umbrella.service.LoggerService;

import java.io.IOException;
import java.util.ArrayList;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private static final String BEARER = "Bearer ";
    private static final String INVALID_TOKEN = "Invalid token";

    private final JwtService jwtService;
    private final LoggerService loggerService;
    private final ApiErrorFactory apiErrorFactory;
    public JwtAuthFilter(JwtService jwtService, LoggerService loggerService, ApiErrorFactory apiErrorFactory) {
        this.jwtService = jwtService;
        this.loggerService = loggerService;
        this.apiErrorFactory = apiErrorFactory;
    }


    /**
     * Performs the filter operation for each incoming request.
     * If the request header does not contain a valid Authorization token, the filter chain is continued.
     * If the request header contains a valid Authorization token:
     *   - The token is validated using the jwtService.validateToken method.
     *   - If the token is valid, the user is authenticated using the authenticateWithToken method.
     *   - The filter chain is continued.
     *   - If the token is invalid, the JwtException is logged using the loggerService.logError method and an JwtException is thrown.
     *
     * @param request The incoming HttpServletRequest.
     * @param response The outgoing HttpServletResponse.
     * @param filterChain The FilterChain to continue the filter flow.
     * @throws ServletException If an error occurs while processing the request.
     * @throws IOException If an error occurs while processing the request.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
            String header = request.getHeader("Authorization");

            if (header == null || !header.startsWith(BEARER)) {
                filterChain.doFilter(request, response);
                return;
            }
            String token = header.replace("Bearer ","");
            try {
                jwtService.validateToken(token);
                authenticateWithToken(token);
                filterChain.doFilter(request, response);
            } catch (JwtException e) {
                loggerService.logError(e, HttpStatus.UNAUTHORIZED);
                throw new JwtException(INVALID_TOKEN);
            }
    }

    private void authenticateWithToken(String token) {
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(null, token, new ArrayList<>());
        SecurityContextHolder.getContext().setAuthentication(auth);
    }
}
