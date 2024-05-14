package org.spring.authservice;

import org.spring.authservice.dto.RefreshTokenResponseDto;
import org.spring.authservice.dto.UserCredentialDto;
import org.spring.authservice.service.IAuthService;
import org.spring.authservice.service.IJwtService;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class AuthFacade {
    private final IAuthService authService;
    private final IJwtService jwtService;

    public AuthFacade(IAuthService authService, IJwtService jwtService) {
        this.authService = authService;
        this.jwtService = jwtService;
    }

    public Mono<RefreshTokenResponseDto> generateTokenIfAuthenticated(UserCredentialDto userCredentialDto) {
        //TODO:Implement token state
        return authService.requestAuthenticatedUser(userCredentialDto)
                .flatMap(userEntityDto -> {
                    String refreshToken = jwtService.generateRefreshToken();
                    authService.saveToken(refreshToken, userEntityDto.getEmail());
                    RefreshTokenResponseDto refreshTokenResponseDto = new RefreshTokenResponseDto();
                    refreshTokenResponseDto.setRefreshToken(refreshToken);
                    return Mono.just(refreshTokenResponseDto);
                });
    }
}
