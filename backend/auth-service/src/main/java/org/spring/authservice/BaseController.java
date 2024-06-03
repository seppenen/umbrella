package org.spring.authservice;

import org.spring.authservice.entity.TokenResponseEntity;
import org.springframework.http.ResponseCookie;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.Map;

import static org.spring.authservice.utility.TokenUtility.ACCESS_TOKEN;
import static org.spring.authservice.utility.TokenUtility.REFRESH_TOKEN;

public abstract class BaseController {

    private static final String STATUS = "status";

    protected <T> Mono<T> send(T response) {
        return Mono.just(response);
    }

    protected Mono<Void> buildResponseWithCookie(TokenResponseEntity tokenResponseEntity, ServerWebExchange exchange) {
        ServerHttpResponse response = exchange.getResponse();
        addCookieToResponse(REFRESH_TOKEN, tokenResponseEntity.refreshToken(), response);
        addCookieToResponse(ACCESS_TOKEN, tokenResponseEntity.accessToken(), response);
        return Mono.empty();
    }

    private void addCookieToResponse(String cookieName, String cookieValue, ServerHttpResponse response) {
        ResponseCookie cookie = ResponseCookie.from(cookieName, cookieValue)
                .httpOnly(true)
                .secure(false)
                .path("/")
                .build();
           response.addCookie(cookie);
    }

    @GetMapping("/health")
    public Mono<Map<String, String>> getServiceHealthStatus() {
        return Mono.just(Collections.singletonMap(STATUS, "UP"));
    }
}
