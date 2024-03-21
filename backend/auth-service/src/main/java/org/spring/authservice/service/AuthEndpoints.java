package org.spring.authservice.service;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthEndpoints {
    @GetMapping("/auth")
    public String Auth() {
        return"auth-service";
    }
}
