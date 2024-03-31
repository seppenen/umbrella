package org.spring.authservice.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.spring.authservice.service.JwtService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    public JwtAuthenticationFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String token = jwtService.resolveToken(request);
        if (token != null && jwtService.validateToken(token)) {
            authenticateWithToken(token);
        }
        filterChain.doFilter(request, response);
    }

    private void authenticateWithToken(String token) {
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(null, token, new ArrayList<>());
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

}
