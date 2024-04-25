package org.umbrella.apigateway.client;


import org.springframework.web.reactive.function.client.WebClient;

public abstract class BaseClient {

    protected static final String AUTHORIZATION_HEADER = "Authorization";
    protected static final String X_REQUEST_HEADER = "X-Request";
    protected static final String HEADER_VALUE = "api-service";

    protected WebClient buildAuthServerWebClient(WebClient client, String token) {
        return client
                .mutate()
                .defaultHeader(AUTHORIZATION_HEADER, "Bearer " + token)
                .defaultHeader(X_REQUEST_HEADER, HEADER_VALUE)
                .build();
    }
}
