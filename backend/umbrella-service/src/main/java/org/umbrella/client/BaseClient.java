package org.umbrella.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.util.Map;

public abstract class BaseClient {
    protected static final String AUTHORIZATION_HEADER = "Authorization";
    protected static final String X_REQUEST_HEADER = "X-Request";
    protected static final String HEADER_VALUE = "umbrella-service";


    protected WebClient buildAuthServerWebClient(WebClient client, String token) {
        return client
                .mutate()
                .defaultHeader(AUTHORIZATION_HEADER, token)
                .defaultHeader(X_REQUEST_HEADER, HEADER_VALUE)
                .build();
    }

    public ResponseStatusException sendErrorResponse(WebClientResponseException e) {
        HttpStatusCode statusCode = e.getStatusCode();
        String respBody = e.getResponseBodyAsString();
        String errorMessage = null;

        if (!respBody.isEmpty()) {
            try {
                var errorResponse = new ObjectMapper().readValue(respBody, Map.class);
                errorMessage = String.valueOf(errorResponse.getOrDefault("detail", errorMessage));
            } catch(Exception ex) {
                throw new HttpMessageNotReadableException("Cannot parse JSON response body",ex);
            }
        }

        return new ResponseStatusException(statusCode, errorMessage);
    }

    protected Mono<Boolean> isResponseStatus2xxSuccessful(ClientResponse response) {
        return Mono.just(response.statusCode().is2xxSuccessful());
    }
}
