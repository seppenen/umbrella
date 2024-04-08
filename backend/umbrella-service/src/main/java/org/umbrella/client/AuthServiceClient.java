package org.umbrella.client;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
@Component
public class AuthServiceClient {
    private final WebClient.Builder authServerClient;

    public AuthServiceClient(WebClient.Builder webClientBuilder) {
        this.authServerClient = webClientBuilder.baseUrl("http://localhost/auth");
    }

    public Mono<Boolean> validateToken(String token) {
        WebClient webClient = buildAuthServerWebClient(token);
        return validateTokenWithWebClient(webClient);
    }

    public Mono<String> requestToken() {
        return this.authServerClient
                .build()
                .post()
                .uri("/api/v1/token")
                .exchangeToMono(response -> response.bodyToMono(String.class));
    }

    private WebClient buildAuthServerWebClient(String token) {
        return this.authServerClient
                .build()
                .mutate()
                .defaultHeaders(headers -> {
                    headers.setBearerAuth(token);
                    headers.set("X-Request", "api-gateway");
                })
                .build();
    }

    private Mono<Boolean> validateTokenWithWebClient(WebClient webClient) {
        return webClient.post()
                .uri("/api/v1/auth")
                .exchangeToMono(this::isResponseStatus2xxSuccessful)
                .onErrorResume(e -> Mono.just(false));
    }

    private Mono<Boolean> isResponseStatus2xxSuccessful(ClientResponse response) {
        return Mono.just(response.statusCode().is2xxSuccessful());
    }
}


