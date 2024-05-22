package org.spring.authservice;

import org.spring.authservice.dto.RefreshTokenResponseDto;
import org.spring.authservice.dto.UserCredentialDto;
import org.spring.authservice.entity.TokenStateEntity;
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

    public Mono<RefreshTokenResponseDto> obtainTokenIfAuthenticated(UserCredentialDto userCredentialDto) {
        return authService.requestAuthenticatedUser(userCredentialDto)
                .flatMap(userEntityDto -> {
                    String refreshToken = jwtService.generateRefreshToken();
                    TokenStateEntity tokenStateEntity = new TokenStateEntity(userEntityDto.getEmail(), refreshToken);
                    authService.updateToken(tokenStateEntity);
                    return Mono.just(new RefreshTokenResponseDto(refreshToken));
                });
    }


}
