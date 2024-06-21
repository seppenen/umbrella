package org.umbrella.client;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.umbrella.exceptionHandlers.GlobalExceptionHandlers;
import reactor.core.publisher.Mono;


@Component
@RequiredArgsConstructor
public class AuthServiceClient extends BaseClient {


    private final WebClient authServerWebClient;

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandlers.class);



    //TODO: Method used for testing purposes only
//    public Mono<Map<String, String>> requestToken() {
//        String token = jwtService.generateAccessToken();
//        return super.buildAuthServerWebClient(authServerWebClient, token)
//                .post()
//                .uri("/token")
//                .retrieve()
//                .bodyToMono(new ParameterizedTypeReference<Map<String, String>>() {
//                });
//    }

    public Mono<Boolean> validateToken(String token) {
        return buildServerWebClient(authServerWebClient, token)
                .post()
                .uri("/authorize")
                .exchangeToMono(this::isResponseStatus2xxSuccessful)
                .onErrorResume(WebClientRequestException.class,
                        e -> Mono.just(false));
        }
}


