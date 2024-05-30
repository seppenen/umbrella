package org.spring.authservice;

import lombok.RequiredArgsConstructor;
import org.spring.authservice.dto.UserCredentialDto;
import org.spring.authservice.entity.TokenStateEntity;
import org.spring.authservice.enums.TokenEnum;
import org.spring.authservice.service.AuthService;
import org.spring.authservice.service.JwtService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import static org.spring.authservice.utility.TokenUtility.ACCESS_TOKEN;
import static org.spring.authservice.utility.TokenUtility.REFRESH_TOKEN;

@Component
@RequiredArgsConstructor
public class AuthFacade {

    private final AuthService authService;
    private final JwtService jwtService;


    public Mono<ResponseEntity<Void>> obtainTokenIfAuthenticated(UserCredentialDto userCredentialDto) {
        String email = userCredentialDto.getEmail();
        return authService.requestAuthenticatedUser(userCredentialDto)
                .flatMap(userEntityDto -> handleTokenOperation(email));
    }

    private Mono<ResponseEntity<Void>> handleTokenOperation(String email) {
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
            return buildResponseWithHeaders(refreshToken, accessToken);
        });
    }


    private Mono<ResponseEntity<Void>> buildResponseWithHeaders(String refreshToken, String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(REFRESH_TOKEN, refreshToken);
        headers.add(ACCESS_TOKEN, accessToken);
        return Mono.just(ResponseEntity.ok().headers(headers).build());
    }
}
