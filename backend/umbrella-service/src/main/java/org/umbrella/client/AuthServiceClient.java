package org.umbrella.client;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.umbrella.service.JwtService;
import reactor.core.publisher.Mono;

import java.util.Map;

@Component
public class AuthServiceClient extends BaseClient {

    private final JwtService jwtService;
    private final WebClient authServerWebClient;


    public AuthServiceClient(JwtService jwtService, WebClient authServerWebClient) {
        this.jwtService = jwtService;
        this.authServerWebClient = authServerWebClient;
    }


    //TODO: Method used for testing purposes only
    public Mono<Map<String, String>> requestToken() {
        String token = jwtService.generateAccessToken();
        return super.buildAuthServerWebClient(authServerWebClient, token)
                .post()
                .uri("/token")
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, String>>() {
                });
    }

    public Mono<Boolean> validateToken(String token) {
        WebClient webClient = super.buildAuthServerWebClient(authServerWebClient, token);
        return webClient.post()
                .uri("/validate")
                .exchangeToMono(this::isResponseStatus2xxSuccessful)
                .onErrorResume(e -> Mono.just(false));
    }

    private Mono<Boolean> isResponseStatus2xxSuccessful(ClientResponse response) {
        return Mono.just(response.statusCode().is2xxSuccessful());
    }
}


