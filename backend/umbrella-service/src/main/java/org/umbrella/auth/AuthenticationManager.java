package org.umbrella.auth;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import org.umbrella.client.AuthServiceClient;



@AllArgsConstructor
@Component
public class AuthenticationManager implements org.springframework.security.authentication.AuthenticationManager {

    private AuthServiceClient authServiceClient;

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationManager.class);
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        AuthToken authenticationToken = (AuthToken) authentication;
        String token = authenticationToken.getPrincipal().toString();
        boolean isTokenValid = Boolean.TRUE.equals(authServiceClient.validateToken(token).block());
        if (!isTokenValid) {
            throw new BadCredentialsException("Invalid token");
        }
        return authenticationToken;
    }
}
