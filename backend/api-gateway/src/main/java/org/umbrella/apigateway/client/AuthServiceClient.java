package org.umbrella.apigateway.client;


import org.apache.http.auth.AuthenticationException;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;


@Component
public class AuthServiceClient extends BaseClient {


    private final WebClient authServiceWebClient;

    public AuthServiceClient( WebClient authServiceWebClient) {
        this.authServiceWebClient = authServiceWebClient;
    }

    public Mono<Boolean> authorize(String token) {
        return buildAuthServerWebClient(authServiceWebClient, token)
                .post()
                .uri("/authorize")
                .exchangeToMono(this::isResponseStatus2xxSuccessful)
                .onErrorResume(e -> Mono.error(new AuthenticationException("Error validating token", e)));
    }

    private Mono<Boolean> isResponseStatus2xxSuccessful(ClientResponse response) {
        return Mono.just(response.statusCode().is2xxSuccessful());
    }


}
