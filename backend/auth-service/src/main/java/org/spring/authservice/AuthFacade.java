package org.spring.authservice;

import lombok.RequiredArgsConstructor;
import org.spring.authservice.dto.UserCredentialDto;
import org.spring.authservice.entity.TokenStateEntity;
import org.spring.authservice.service.AuthService;
import org.spring.authservice.service.JwtService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import static org.spring.authservice.utility.TokenUtility.ACCESS_TOKEN;
import static org.spring.authservice.utility.TokenUtility.ACCESS_TOKEN_EXPIRE_TIME;
import static org.spring.authservice.utility.TokenUtility.REFRESH_TOKEN;
import static org.spring.authservice.utility.TokenUtility.REFRESH_TOKEN_EXPIRE_TIME;

@Component
@RequiredArgsConstructor
public class AuthFacade {

    private final AuthService authService;
    private final JwtService jwtService;


    public Mono<ResponseEntity<Void>> obtainTokenIfAuthenticated(UserCredentialDto userCredentialDto) {
        return authService.requestAuthenticatedUser(userCredentialDto)
                .flatMap(userEntityDto -> Mono.zip(
                        jwtService.generateToken(ACCESS_TOKEN_EXPIRE_TIME),
                        jwtService.generateToken(REFRESH_TOKEN_EXPIRE_TIME)
                ).flatMap(tokens -> {
                    String refreshToken = tokens.getT1();
                    String accessToken = tokens.getT2();
                    String email = userEntityDto.getEmail();
                    TokenStateEntity tokenStateEntity = new TokenStateEntity(refreshToken, email);
                    authService.updateToken(tokenStateEntity);
                    return buildResponseWithHeaders(refreshToken, accessToken);
                }));
    }

    private Mono<ResponseEntity<Void>> buildResponseWithHeaders(String refreshToken, String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(REFRESH_TOKEN, refreshToken);
        headers.add(ACCESS_TOKEN, accessToken);
        return Mono.just(ResponseEntity.ok().headers(headers).build());
    }
}
