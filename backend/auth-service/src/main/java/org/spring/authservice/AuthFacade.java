package org.spring.authservice;

import lombok.RequiredArgsConstructor;
import org.spring.authservice.dto.UserCredentialDto;
import org.spring.authservice.entity.AuthenticationTokenData;
import org.spring.authservice.entity.TokenResponseEntity;
import org.spring.authservice.enums.TokenEnum;
import org.spring.authservice.service.AuthService;
import org.spring.authservice.service.JwtService;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class AuthFacade {

    private final AuthService authService;
    private final JwtService jwtService;


    public Mono<TokenResponseEntity> obtainTokensIfAuthenticated(UserCredentialDto userCredentialDto) {
        String email = userCredentialDto.getEmail();
        return authService.requestAuthenticatedUser(userCredentialDto)
                .flatMap(userEntityDto -> handleTokenOps(email));
    }

    private Mono<TokenResponseEntity> handleTokenOps(String email) {
        return Mono.zip(
                jwtService.generateToken(
                        TokenEnum.REFRESH_TOKEN_EXPIRE_TIME.getAsInteger(),
                        TokenEnum.REFRESH_TOKEN_TYPE.getAsString(),
                        email),
                jwtService.generateToken(
                        TokenEnum.ACCESS_TOKEN_EXPIRE_TIME.getAsInteger(),
                        TokenEnum.ACCESS_TOKEN_TYPE.getAsString(),
                        email)
        ).doOnNext(tokens -> {
            AuthenticationTokenData tokenStateEntity = new AuthenticationTokenData(tokens.getT1(), email);
            authService.updateToken(tokenStateEntity);
        }).map(tokens -> new TokenResponseEntity(tokens.getT1(), tokens.getT2()));
    }
}
