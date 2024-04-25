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

        //TODO:Implement token state
        return authService.requestAuthenticatedUser(userCredentialDto)
                .flatMap(userEntityDto -> {
                    String refreshToken = jwtService.generateRefreshToken();
                    //authService.saveToken(refreshToken, userEntityDto.getUsername());
                    RefreshTokenResponseDto refreshTokenResponseDto = new RefreshTokenResponseDto();
                    refreshTokenResponseDto.setRefreshToken(refreshToken);
                    return Mono.just(refreshTokenResponseDto);
                });
    }
}
