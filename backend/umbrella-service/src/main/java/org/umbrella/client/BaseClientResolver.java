package org.umbrella.client;

import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public abstract class BaseClientResolver {
    protected static final String AUTHORIZATION_HEADER = "Authorization";
    protected static final String X_REQUEST_HEADER = "X-Request";
    protected static final String HEADER_VALUE = "umbrella-service";


    protected WebClient buildServerWebClient(WebClient client, String token) {
        return client
                .mutate()
                .defaultHeader(AUTHORIZATION_HEADER, "Bearer " + token)
                .defaultHeader(X_REQUEST_HEADER, HEADER_VALUE)
                .build();
    }


    protected Mono<Boolean> isResponseStatus2xxSuccessful(ClientResponse response) {
        return Mono.just(response.statusCode().is2xxSuccessful());
    }
}
