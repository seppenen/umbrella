package org.spring.authservice;

import lombok.AllArgsConstructor;
import org.spring.authservice.dto.UserCredentialDto;
import org.spring.authservice.entity.AccessTokenEntity;
import org.spring.authservice.entity.TokenStateEntity;
import org.spring.authservice.service.AuthService;
import org.spring.authservice.service.JwtService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple3;

@Component
@AllArgsConstructor
public class AuthFacade {

    private AuthService authService;
    private JwtService jwtService;

    /**
     * Generate and persist tokens for an authenticated user.
     *
     * @param userCredentialDto the user credential data transfer object
     * @return a Mono emitting a Tuple3 object containing the tokens (refresh token, access token, email)
     */
    public Mono<Tuple3<String, String, String>> processTokensIfAuthenticatedUser(UserCredentialDto userCredentialDto) {
        return authService.requestAuthenticatedUser(userCredentialDto)
                .flatMap(userEntityDto -> jwtService.obtainTokens(userEntityDto.getEmail()))
                .doOnNext(this::persistAndEvictRefreshToken)
                .doOnNext(this::persistAccessToken);
    }

    private void persistAccessToken(Tuple3<String, String, String> tokens) {
        AccessTokenEntity accessTokenEntity = new AccessTokenEntity(null, tokens.getT2());
        authService.persistAccessToken(accessTokenEntity);
    }

    @Transactional
    public void persistAndEvictRefreshToken(Tuple3<String, String, String> tokens) {
        TokenStateEntity tokenStateEntity = new TokenStateEntity(tokens.getT1(), tokens.getT3());
        authService.persistRefreshToken(tokenStateEntity);
        authService.evictOldRefreshTokens(tokenStateEntity);
    }
}
