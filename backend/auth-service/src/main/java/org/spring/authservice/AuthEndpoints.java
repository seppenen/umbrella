package org.spring.authservice;

import org.spring.authservice.service.JwtService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class AuthEndpoints {

    private final JwtService jwtService;

    public AuthEndpoints(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @PostMapping("/auth")
    public Mono<Map<String, String>> authenticate() {
        // Assuming the token has been validated by Spring Security filters already...
        Map<String, String> response = new HashMap<>();
        response.put("status", "success");
        response.put("isAuthenticated", "true");
        return Mono.just(response);
    }

    @GetMapping("/token")
    //TODO: forbid this endpoint in production
    public Mono<Map<String, Object>> getToken() {
        Map<String, Object> access_token = new HashMap<>();
        String token = jwtService.generateToken();
        access_token.put("access_token", token);
        return Mono.just(access_token);
    }

    @GetMapping("/health")
    public Flux<Map<String, Object>> getHealth() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        return Flux.just(response);
    }
}


