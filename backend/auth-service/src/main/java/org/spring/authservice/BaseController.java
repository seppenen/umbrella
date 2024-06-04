package org.spring.authservice;

import org.springframework.http.ResponseCookie;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple3;

import java.util.Collections;
import java.util.Map;

import static org.spring.authservice.utility.TokenUtility.ACCESS_TOKEN;
import static org.spring.authservice.utility.TokenUtility.REFRESH_TOKEN;

public abstract class BaseController {

    private static final String STATUS = "status";

    protected <T> Mono<T> sendMono(T response) {
        return Mono.just(response);
    }

    protected <T> Flux<T> sendFlux(T response) {
        return Flux.just(response);
    }

    protected Mono<Void> buildResponseWithCookie(Tuple3<String, String, String> tokens, ServerWebExchange exchange) {
        ServerHttpResponse response = exchange.getResponse();
        addCookieToResponse(REFRESH_TOKEN, tokens.getT1(), response);
        addCookieToResponse(ACCESS_TOKEN, tokens.getT2(), response);
        return Mono.empty();
    }

    private void addCookieToResponse(String cookieName, String cookieValue, ServerHttpResponse response) {
        ResponseCookie cookie = ResponseCookie.from(cookieName, cookieValue)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .build();
           response.addCookie(cookie);
    }

    @GetMapping("/health")
    public Mono<Map<String, String>> getServiceHealthStatus() {
        return Mono.just(Collections.singletonMap(STATUS, "UP"));
    }
}
