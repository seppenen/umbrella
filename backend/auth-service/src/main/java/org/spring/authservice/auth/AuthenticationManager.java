package org.spring.authservice.auth;

import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.AllArgsConstructor;
import org.spring.authservice.entity.TokenStateEntity;
import org.spring.authservice.service.JwtService;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.List;

@Component
@AllArgsConstructor
public class AuthenticationManager implements ReactiveAuthenticationManager {

    private static final String DEFAULT_ROLE = "ROLE_USER";
    private final JwtService tokenService;

    /**
     * Authenticates the provided authentication object.
     *
     * @param authentication The authentication object to be authenticated.
     * @return A Mono of type Authentication representing the authenticated authentication object.
     */
    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        return Mono.just(authentication.getCredentials().toString())
                .flatMap(this::verifyToken)
                .flatMap(this::getAuthentication);
    }

    private Mono<Authentication> getAuthentication(TokenStateEntity appUser) {
        return Mono.just(new UsernamePasswordAuthenticationToken(appUser, null,
                        getGrantedAuthorities())
        );
    }

    private Collection<SimpleGrantedAuthority> getGrantedAuthorities() {
        return List.of(new SimpleGrantedAuthority(DEFAULT_ROLE));
    }

    private Mono<TokenStateEntity> verifyToken(String token) {
        return tokenService.validateToken(token)
                .filter(decodedJWT -> decodedJWT.getIssuer() != null && !decodedJWT.getSignature().isEmpty())
                .flatMap(tokenData -> getAppUser(tokenData, token));
    }

    private Mono<TokenStateEntity> getAppUser(DecodedJWT decodedJWT, String token) {
        return Mono.just(TokenStateEntity.builder()
                .email(decodedJWT.getClaim("principal").asString())
                .token(token)
                .build()
        );
    }
}
