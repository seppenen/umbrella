package org.spring.authservice;

import org.spring.authservice.entity.TokenResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.Map;

import static org.spring.authservice.utility.TokenUtility.ACCESS_TOKEN;
import static org.spring.authservice.utility.TokenUtility.REFRESH_TOKEN;

public abstract class BaseController {

    private static final String STATUS = "status";

    protected Mono<ResponseEntity<Void>> buildResponseWithHeaders(TokenResponseEntity tokenResponseEntity) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(REFRESH_TOKEN, tokenResponseEntity.refreshToken());
        headers.add(ACCESS_TOKEN, tokenResponseEntity.accessToken());
        return Mono.just(ResponseEntity.ok().headers(headers).build());
    }

    @GetMapping("/health")
    public Mono<Map<String, String>> getServiceHealthStatus() {
        return Mono.just(Collections.singletonMap(STATUS, "UP"));
    }
}
