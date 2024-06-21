package org.umbrella.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.umbrella.auth.AuthToken;
import org.umbrella.auth.AuthenticationManager;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class AuthServiceFilter extends OncePerRequestFilter {

    private final AuthenticationManager authenticationManager;
    private static final String AUTH_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain)
            throws ServletException, IOException {

        //TODO: implement headers without bearer prefix

        String authHeader = request.getHeader(AUTH_HEADER);

        if(authHeader != null && authHeader.startsWith(BEARER_PREFIX)) {
            String token = authHeader.substring("Bearer ".length());
            String userName = "alex@gmail.com"; // JwtUtils.extractUserName(token);
            AuthToken authToken = new AuthToken(token, userName);
            try {
                 authenticationManager.authenticate(authToken);
            } catch (AuthenticationException e) {
                throw new RuntimeException(e);
            }
        }
        chain.doFilter(request, response);
    }

}
