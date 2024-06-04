package org.spring.authservice;

import lombok.AllArgsConstructor;
import org.spring.authservice.auth.AuthTokenManager;
import org.spring.authservice.dto.UserCredentialDto;
import org.spring.authservice.service.AuthService;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple3;

@Component
@AllArgsConstructor
public class AuthFacade {

    private AuthService authService;
    private AuthTokenManager authTokenManager;

    /**
     * Obtains the refresh token and access token for an authenticated user.
     *
     * @param userCredentialDto the user's credentials
     * @return a Mono emitting a TokenResponseEntity with the refresh token and access token
     */
    public Mono<Tuple3<String, String, String>> obtainTokensIfAuthenticated(UserCredentialDto userCredentialDto) {
        return authService.requestAuthenticatedUser(userCredentialDto)
                .flatMap(userEntityDto -> authTokenManager.obtainTokens(userEntityDto.getEmail()))
                .flatMap(tokens -> authTokenManager.persistsTokens(tokens)
                                .thenReturn(tokens));
    }
}
