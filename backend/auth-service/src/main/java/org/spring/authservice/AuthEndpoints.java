package org.spring.authservice;

import org.spring.authservice.service.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<Map<String, Object>> authenticate() {
        // Assuming the token has been validated by Spring Security filters already...
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("isAuthenticated", true);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/token")
    //TODO: forbid this endpoint in production
    public String getToken() {
        return jwtService.generateToken();
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> getHealth() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        return ResponseEntity.ok(response);
    }
}


