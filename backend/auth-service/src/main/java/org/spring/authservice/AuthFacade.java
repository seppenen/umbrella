package org.spring.authservice;

import org.spring.authservice.service.JwtService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.Map;

@Service
public class AuthFacade {

    private static final String STATUS = "status";
    private static final String IS_AUTHENTICATED = "isAuthenticated";
    private static final String ACCESS_TOKEN = "access_token";
    private final JwtService jwtService;

    private static final Mono<Map<String, String>> AUTHENTICATE_RESPONSE = Mono.fromSupplier(() ->
            Map.of(STATUS, "success", IS_AUTHENTICATED, "true")
    );

    private static final Mono<Map<String, Object>> HEALTH_STATUS_RESPONSE = Mono.fromSupplier(() ->
            Collections.singletonMap(STATUS, "UP")
    );

    public AuthFacade(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    public Mono<Map<String, String>> authenticate() {
        return AUTHENTICATE_RESPONSE;
    }

    public Mono<Map<String, Object>> generateToken() {
        return Mono.just(Collections.singletonMap(ACCESS_TOKEN, jwtService.generateToken()));
    }

    public Mono<Map<String, Object>> getHealthStatus() {
        return HEALTH_STATUS_RESPONSE;
    }
}
