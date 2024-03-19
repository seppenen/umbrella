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
