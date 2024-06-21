package org.umbrella.auth;

import lombok.AllArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.umbrella.client.AuthServiceClient;



@AllArgsConstructor
@Component
public class AuthenticationManager implements org.springframework.security.authentication.AuthenticationManager {

    private AuthServiceClient authServiceClient;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        AuthToken authToken = (AuthToken) authentication;
        String token = authToken.getPrincipal().toString();
        boolean isTokenValid = Boolean.TRUE.equals(authServiceClient.validateToken(token).block());
        if (!isTokenValid) {
            throw new BadCredentialsException("Invalid token");
        }
        authToken.setAuthenticated(true);
        SecurityContextHolder.getContext().setAuthentication(authToken);
        return authToken;
    }
}
