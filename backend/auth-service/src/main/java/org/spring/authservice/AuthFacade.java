package org.spring.authservice;

import org.spring.authservice.dto.UserCredentialDto;
import org.spring.authservice.service.AuthService;
import org.spring.authservice.service.JwtService;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.Map;

@Component
public class AuthFacade {
    private final AuthService authService;
    private final JwtService jwtService;

    public AuthFacade(AuthService authService, JwtService jwtService) {
        this.authService = authService;
        this.jwtService = jwtService;
    }

    public Mono<Map<String, String>> authenticate(UserCredentialDto userCredentialDto) {
        return authService.authenticate(userCredentialDto)
                .then(Mono.just(Collections.singletonMap("token", jwtService.generateRefreshToken())));
    }
}
