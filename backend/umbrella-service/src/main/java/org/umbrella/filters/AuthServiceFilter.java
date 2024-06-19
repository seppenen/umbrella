package org.umbrella.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.umbrella.auth.AuthToken;
import org.umbrella.auth.AuthenticationManager;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class AuthServiceFilter extends OncePerRequestFilter {

    private final AuthenticationManager authenticationManager;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if(authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring("Bearer ".length());
            String userName = "alex@gmail.com"; // JwtUtils.extractUserName(token);
            AuthToken authToken = new AuthToken(token, userName);
            Authentication auth = null;
            try {
                auth = authenticationManager.authenticate(authToken);
            } catch (AuthenticationException e) {
                throw new RuntimeException(e);
            }
            if (auth != null) {
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }

        chain.doFilter(request, response);
    }

}
