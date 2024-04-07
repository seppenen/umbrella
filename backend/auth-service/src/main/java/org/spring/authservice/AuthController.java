package org.spring.authservice;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class AuthController {
    private final AuthFacade authFacade;

    public AuthController(AuthFacade authFacade) {
        this.authFacade = authFacade;
    }
    @PostMapping("/auth")
    public Mono<Map<String, String>> authenticate() {
        return authFacade.authenticate();
    }

    @GetMapping("/token")
    //TODO: forbid this endpoint in production
    public Mono<Map<String, Object>> getToken() {
        return authFacade.generateToken();
    }

    @GetMapping("/health")
    public Mono<Map<String, Object>> getHealth() {
        return authFacade.getHealthStatus();
    }
}


