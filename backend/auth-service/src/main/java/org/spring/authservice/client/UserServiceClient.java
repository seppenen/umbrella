package org.spring.authservice.client;


import lombok.RequiredArgsConstructor;
import org.spring.authservice.dto.UserCredentialDto;
import org.spring.authservice.dto.UserEntityDto;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;


@Component
@RequiredArgsConstructor
public class UserServiceClient extends BaseClientResolver {

    private final WebClient userServiceWebClient;


    public Mono<UserEntityDto> requestUserAuthentication(UserCredentialDto userCredentialDto) {
        return userServiceWebClient.post()
                .uri("/login")
                .bodyValue(userCredentialDto)
                .retrieve()
                .onStatus(this::isClientOrServerError, clientResponse ->
                       Mono.error(new ResponseStatusException(clientResponse.statusCode(), "Unable to authenticate user")))
                .bodyToMono(UserEntityDto.class);
    }
}
