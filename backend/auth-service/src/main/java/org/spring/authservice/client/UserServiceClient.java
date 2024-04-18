package org.spring.authservice.client;


import org.spring.authservice.dto.UserCredentialDto;
import org.spring.authservice.dto.UserEntityDto;
import org.spring.authservice.entity.ApiErrorResponse;
import org.spring.authservice.exceptions.HttpRequestException;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;


@Component
public class UserServiceClient extends BaseClientResolver {

    private final WebClient userServiceWebClient;

    public UserServiceClient(WebClient userServiceWebClient) {
        this.userServiceWebClient = userServiceWebClient;
    }

    public Mono<UserEntityDto> requestUserAuthentication(UserCredentialDto userCredentialDto) {
        return userServiceWebClient.post()
                .uri("/login")
                .bodyValue(userCredentialDto)
                .retrieve()
                .onStatus(this::isClientOrServerError, clientResponse ->
                      clientResponse.bodyToMono(ApiErrorResponse.class)
                              .flatMap(errorResponse -> Mono.error(new HttpRequestException(errorResponse.getErrorCode(), errorResponse.getMessage()))))
                .bodyToMono(UserEntityDto.class);
    }
}
