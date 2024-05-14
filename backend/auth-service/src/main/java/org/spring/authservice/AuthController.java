package org.spring.authservice;

import org.spring.authservice.dto.RefreshTokenResponseDto;
import org.spring.authservice.dto.UserCredentialDto;
import org.spring.authservice.service.impl.JwtService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.Map;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/v1")
public class AuthController {
    private static final String STATUS = "status";
    private static final String TOKEN_VALID = "tokenValid";
    private static final String TOKEN_KEY = "access_token";
    private final JwtService jwtService;

    private final AuthFacade authFacade;

    public AuthController(JwtService jwtService, AuthFacade authFacade) {
        this.jwtService = jwtService;
        this.authFacade = authFacade;
    }

    //TODO: this method validate access token
    @PostMapping("/authorize")
    public Mono<Map<String, Object>> validateToken() {
        return Mono.just(Map.of(STATUS, "success", TOKEN_VALID, true));
    }

    //TODO:This method creates a new refresh token
    @PostMapping("/authenticate")
    public Mono<RefreshTokenResponseDto> authenticate(@RequestBody UserCredentialDto userRequestDto) {
        return authFacade.generateTokenIfAuthenticated(userRequestDto);
    }

    @PostMapping("/access-token")
    public Mono<Map<String, String>> getAccessToken() {
        return Mono.just(Collections.singletonMap(TOKEN_KEY, jwtService.generateAccessToken()));
    }

    @GetMapping("/health")
    public Mono<Map<String, String>> getServiceHealthStatus() {
        return Mono.just(Collections.singletonMap(STATUS, "UP"));
    }
}


