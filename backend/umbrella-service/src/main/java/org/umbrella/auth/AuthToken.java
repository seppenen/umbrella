package org.umbrella.auth;

import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;

import java.util.Collections;


public class AuthToken extends AbstractAuthenticationToken {

    private String token;
    @Getter
    private String user;


    public AuthToken(String token, String user) {
        super(Collections.emptyList());
        this.user = user;
        this.token = token;
    }


    @Override
    public Object getCredentials() {
        return token;
    }

    @Override
    public Object getPrincipal() {
        return token;
    }
}
