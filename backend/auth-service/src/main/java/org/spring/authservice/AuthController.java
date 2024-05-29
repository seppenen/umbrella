package org.spring.authservice;

import org.spring.authservice.dto.UserCredentialDto;
import org.spring.authservice.service.JwtService;
import org.spring.authservice.service.impl.JwtServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.Map;

import static org.spring.authservice.utility.TokenUtility.ACCESS_TOKEN;
import static org.spring.authservice.utility.TokenUtility.ACCESS_TOKEN_EXPIRE_TIME;
import static org.spring.authservice.utility.TokenUtility.TOKEN_VALID;


@RestController
@CrossOrigin("*")
@RequestMapping("/api/v1")
public class AuthController {
    private static final String STATUS = "status";

    private final JwtService jwtService;

    private final AuthFacade authFacade;

    public AuthController(JwtServiceImpl jwtService, AuthFacade authFacade) {
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
    public Mono<ResponseEntity<Void>> authenticate(@RequestBody UserCredentialDto userRequestDto) {
        return authFacade.obtainTokenIfAuthenticated(userRequestDto);
    }

    @PostMapping("/access-token")
    public Mono<ResponseEntity<Void>> getAccessToken() {
        return jwtService.generateToken(ACCESS_TOKEN_EXPIRE_TIME)
                .map(accessToken -> ResponseEntity.ok()
                        .header(ACCESS_TOKEN, accessToken)
                        .build());
    }

    @GetMapping("/health")
    public Mono<Map<String, String>> getServiceHealthStatus() {
        return Mono.just(Collections.singletonMap(STATUS, "UP"));
    }
}


