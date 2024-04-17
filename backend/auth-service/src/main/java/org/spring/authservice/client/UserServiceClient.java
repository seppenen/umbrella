package org.spring.authservice.client;


import org.spring.authservice.dto.UserCredentialDto;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;


@Component
public class UserServiceClient extends BaseClient {

    private final WebClient userServiceWebClient;

    public UserServiceClient(WebClient userServiceWebClient) {
        this.userServiceWebClient = userServiceWebClient;
    }

    public Mono<Void> validateUserCredentials(UserCredentialDto userCredentialDto) {
        return userServiceWebClient.post()
                .uri("/login")
                .bodyValue(userCredentialDto)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<>() {});
    }
}
