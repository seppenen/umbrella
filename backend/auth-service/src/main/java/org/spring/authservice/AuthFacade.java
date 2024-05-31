package org.spring.authservice;

import lombok.RequiredArgsConstructor;
import org.spring.authservice.dto.UserCredentialDto;
import org.spring.authservice.entity.TokenResponseEntity;
import org.spring.authservice.entity.TokenStateEntity;
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
        ).flatMap(tokens -> {
            String refreshToken = tokens.getT1();
            String accessToken = tokens.getT2();
            TokenStateEntity tokenStateEntity = new TokenStateEntity(refreshToken, email);
            authService.updateToken(tokenStateEntity);
            return Mono.just(new TokenResponseEntity(refreshToken, accessToken));
        });
    }
}
