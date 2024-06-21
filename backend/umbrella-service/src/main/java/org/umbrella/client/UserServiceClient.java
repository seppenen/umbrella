package org.umbrella.client;

import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.umbrella.dto.UserRequestDto;

import java.util.List;

@Component
@RequiredArgsConstructor
public class UserServiceClient extends BaseClient {

    private final WebClient userServiceWebClient;

    public List<UserRequestDto> getUsers(String token) {
        WebClient webClient = super.buildServerWebClient(userServiceWebClient, token);
        return webClient.get()
                .uri("/users")
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<UserRequestDto>>() {
                }).block();
    }
}

