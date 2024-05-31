package org.spring.authservice.client;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

public abstract class BaseClientResolver {

    protected static final String AUTHORIZATION_HEADER = "Authorization";
    protected static final String X_REQUEST_HEADER = "X-Request";
    protected static final String HEADER_VALUE = "auth-service";


    public ResponseStatusException handle(WebClientResponseException e) {
        try {
            String respBody = e.getResponseBodyAsString();
            if (!respBody.isBlank()) {
                var errorResponse = new ObjectMapper().readValue(respBody, Map.class);
                String errorMessage = String.valueOf(errorResponse.get("detail"));
                return new ResponseStatusException(e.getStatusCode(), errorMessage);
            }
        } catch (Exception ex) {
            throw new HttpMessageNotReadableException("Cannot parse JSON response body");
        }
        return null;
    }


    protected WebClient buildAuthServerWebClient(WebClient client) {
        return client
                .mutate()
              //  .defaultHeader(AUTHORIZATION_HEADER, "Bearer " + token)
                .defaultHeader(X_REQUEST_HEADER, HEADER_VALUE)
                .build();
    }
}
