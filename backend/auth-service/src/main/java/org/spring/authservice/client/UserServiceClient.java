package org.spring.authservice.client;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.spring.authservice.dto.UserCredentialDto;
import org.spring.authservice.dto.UserEntityDto;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.util.Map;


@Component
@RequiredArgsConstructor
public class UserServiceClient extends BaseClientResolver {

    private final WebClient userServiceWebClient;


    public Mono<UserEntityDto> requestUserAuthentication(UserCredentialDto userCredentialDto) {
        return userServiceWebClient.post()
                .uri("/login")
                .bodyValue(userCredentialDto)
                .retrieve()
                .bodyToMono(UserEntityDto.class)
                .onErrorResume(WebClientResponseException.class, e -> {
                    try {
                        String respBody = e.getResponseBodyAsString();
                        if (!respBody.isBlank()) {
                            var errorResponse = new ObjectMapper().readValue(respBody, Map.class);
                            String errorMessage = String.valueOf(errorResponse.get("detail"));
                            return Mono.error(new ResponseStatusException(e.getStatusCode(), errorMessage));
                        }
                    }
                    catch (JsonProcessingException ex) {
                        throw new HttpMessageNotReadableException("Cannot parse JSON response body");
                    }
                    return Mono.error(e);
                });
    }
}
