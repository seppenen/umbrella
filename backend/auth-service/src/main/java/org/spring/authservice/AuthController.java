package org.spring.authservice;

import org.spring.authservice.service.JwtService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class AuthController {
    private static final String STATUS = "status";
    private static final String TOKEN_VALID = "tokenValid";
    private static final String TOKEN_KEY = "access_token";
    private final JwtService jwtService;

    public AuthController(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @PostMapping("/auth")
    public Mono<Map<String, Object>> validateToken() {
        return Mono.just(Map.of(STATUS, "success", TOKEN_VALID, true));
    }

    @GetMapping("/token")
    //TODO: forbid this endpoint in production
    public Mono<Map<String, String>> generateAuthenticationToken() {
        return Mono.just(Collections.singletonMap(TOKEN_KEY, jwtService.generateToken()));
    }

    @GetMapping("/health")
    public Mono<Map<String, String>> getServiceHealthStatus() {
        return Mono.just(Collections.singletonMap(STATUS, "UP"));
    }
}


