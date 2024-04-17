package org.spring.authservice;

import org.spring.authservice.dto.RefreshTokenResponseDto;
import org.spring.authservice.dto.UserCredentialDto;
import org.spring.authservice.service.AuthService;
import org.spring.authservice.service.JwtService;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class AuthFacade {
    private final AuthService authService;
    private final JwtService jwtService;

    public AuthFacade(AuthService authService, JwtService jwtService) {
        this.authService = authService;
        this.jwtService = jwtService;
    }

    public Mono<RefreshTokenResponseDto> generateTokenIfAuthenticated(UserCredentialDto userCredentialDto) {
        return authService.requestAuthenticatedUser(userCredentialDto)
                .flatMap(userEntityDto -> {
                    String accessToken = jwtService.generateAccessToken();
                    RefreshTokenResponseDto refreshTokenResponseDto = new RefreshTokenResponseDto();
                    refreshTokenResponseDto.setRefresh_token(accessToken);
                    return Mono.just(refreshTokenResponseDto);
                });
    }
}
