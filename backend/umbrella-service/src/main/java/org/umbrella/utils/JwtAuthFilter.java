package org.umbrella.utils;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.umbrella.entity.ApiErrorResponse;

import java.io.IOException;
import java.util.ArrayList;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {


    private final JwtService jwtService;
    private final ApiErrorFactory apiErrorFactory;
    public JwtAuthFilter(JwtService jwtService, ApiErrorFactory apiErrorFactory) {
        this.jwtService = jwtService;
        this.apiErrorFactory = apiErrorFactory;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String header = request.getHeader("Authorization");

        if (header == null || !header.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = header.replace("Bearer ", "");

        try {
            if (jwtService.validateToken(token)) {
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(null, token, new ArrayList<>());
                SecurityContextHolder.getContext().setAuthentication(auth);
            } else {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token");
                return;
            }
        } catch (Exception ex) {
            ApiErrorResponse apiErrorResponse = apiErrorFactory.create(
                    HttpStatus.UNAUTHORIZED,
                    ex.getMessage(),
                    "The provided token is not valid");

            StringBuilder errorResponse = new StringBuilder();
            errorResponse.append(apiErrorResponse);
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write(errorResponse.toString());
            return;
        }

        filterChain.doFilter(request, response);
    }
}
