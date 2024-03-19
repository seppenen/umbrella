package org.umbrella.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
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
    public JwtAuthFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
            String header = request.getHeader("Authorization");

            if (header == null || !header.startsWith("Bearer ")) {
                filterChain.doFilter(request, response);
                return;
            }
            String token = header.replace("Bearer ","");
        try {
            jwtService.validateToken(token);
            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(null, token, new ArrayList<>());
            SecurityContextHolder.getContext().setAuthentication(auth);
            } catch (Exception ex) {
                writeAuthException(response, HttpServletResponse.SC_UNAUTHORIZED, "Error authenticating: ");
            return;
        }

            filterChain.doFilter(request, response);
        }

    private void writeAuthException(HttpServletResponse response, int status, String errorMessage) throws IOException {
        ApiErrorResponse apiErrorResponse = ApiErrorFactory.create(HttpStatus.UNAUTHORIZED, errorMessage, "The provided token is not valid");
        ObjectMapper objectMapper = new ObjectMapper();
        String errorResponse = objectMapper.writeValueAsString(apiErrorResponse);

        response.setContentType("application/json");
        response.setStatus(status);
        response.getWriter().write(errorResponse);
    }

}
