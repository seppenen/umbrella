package org.spring.authservice.client;


import org.springframework.http.HttpStatusCode;
import org.springframework.web.reactive.function.client.WebClient;

public abstract class BaseClientResolver {



    protected static final String AUTHORIZATION_HEADER = "Authorization";
    protected static final String X_REQUEST_HEADER = "X-Request";
    protected static final String HEADER_VALUE = "auth-service";


    protected boolean isClientOrServerError(HttpStatusCode httpStatus) {
        return httpStatus.is4xxClientError() || httpStatus.is5xxServerError();
    }


    protected WebClient buildAuthServerWebClient(WebClient client) {
        return client
                .mutate()
              //  .defaultHeader(AUTHORIZATION_HEADER, "Bearer " + token)
                .defaultHeader(X_REQUEST_HEADER, HEADER_VALUE)
                .build();
    }
}
