package org.umbrella.apigateway.client;


import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;


@Component
public class AuthServiceClient extends BaseClientResolver {

    private final WebClient authServiceWebClient;

    public AuthServiceClient( WebClient authServiceWebClient) {
        this.authServiceWebClient = authServiceWebClient;
    }

    public Mono<Boolean> authorize(String token) {
        return buildAuthServerWebClient(authServiceWebClient, token)
                .post()
                .uri("/authorize")
                .exchangeToMono(this::isResponseStatus2xxSuccessful)
                .onErrorResume(e -> Mono.error(new ResponseStatusException(HttpStatus.BAD_GATEWAY, e.getMessage())));
    }

}
