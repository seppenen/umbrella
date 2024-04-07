package org.spring.authservice;

import org.spring.authservice.service.JwtService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.Map;

@Service
public class AuthFacade {

    private static final String STATUS = "status";
    private static final String TOKEN_VALID = "tokenValid";
    private static final String TOKEN_KEY = "token";
    private final JwtService jwtService;

    public AuthFacade(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    public Mono<Map<String, String>> validateToken() {
        return Mono.just(Map.of(
                STATUS, "success",
                TOKEN_VALID, "true")
        );
    }

    public Mono<Map<String, Object>> createNewToken() {
        return Mono.just(Collections.singletonMap(
                TOKEN_KEY,
                jwtService.generateToken()
        ));
    }

    public Mono<Map<String, Object>> checkServiceHealth() {
        return Mono.just(Collections.singletonMap(STATUS, "UP"));
    }
}
