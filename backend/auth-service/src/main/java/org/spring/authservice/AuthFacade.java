package org.spring.authservice;

import lombok.AllArgsConstructor;
import org.spring.authservice.auth.AuthTokenManager;
import org.spring.authservice.dto.UserCredentialDto;
import org.spring.authservice.entity.AccessTokenEntity;
import org.spring.authservice.entity.TokenStateEntity;
import org.spring.authservice.service.AuthService;
import org.spring.authservice.service.JwtService;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple3;

@Component
@AllArgsConstructor
public class AuthFacade {

    private AuthService authService;
    private JwtService jwtService;
    private AuthTokenManager authTokenManager;

    /**
     * Obtains the refresh token and access token for an authenticated user.
     *
     * @param userCredentialDto the user's credentials
     * @return a Mono emitting a TokenResponseEntity with the refresh token and access token
     */
    public Mono<Tuple3<String, String, String>> getTokensForAuthenticatedUser(UserCredentialDto userCredentialDto) {
        return authService.requestAuthenticatedUser(userCredentialDto)
                .flatMap(userEntityDto -> jwtService.obtainTokens(userEntityDto.getEmail()))
                .doOnNext(this::persistRefreshToken)
                .doOnNext(this::persistAccessToken);
    }

    private void persistRefreshToken(Tuple3<String, String, String> tokens) {
        TokenStateEntity tokenStateEntity = new TokenStateEntity(tokens.getT1(), tokens.getT3());
        authTokenManager.persistRefreshToken(tokenStateEntity);
    }

    private void persistAccessToken(Tuple3<String, String, String> tokens) {
        AccessTokenEntity accessTokenEntity = new AccessTokenEntity(null, tokens.getT2());
        authTokenManager.persistAccessToken(accessTokenEntity);
    }

    public void updateTokens(Tuple3<String, String, String> tokens) {
        TokenStateEntity authenticationTokenData = new TokenStateEntity(tokens.getT1(), tokens.getT3());
         authTokenManager.updateRefreshToken(authenticationTokenData);
    }
}
