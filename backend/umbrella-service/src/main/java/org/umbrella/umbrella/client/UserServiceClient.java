package org.umbrella.umbrella.client;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.umbrella.umbrella.dto.UserRequestDto;

import java.util.List;

@Component
public class UserServiceClient {
    private final RestClient userServerClient;

    /**
     * This class represents a client for the User Service API.
     * It provides methods to interact with the User Service server.
     */
    public UserServiceClient() {
        userServerClient = RestClient.builder()
                .baseUrl("http://user-service:8080")
                .build();
    }

    /**
     * Retrieves the list of users from the server.
     *
     * @return the list of users as a UserResponseDto object
     */
    public List<UserRequestDto> getUsers() {
        return userServerClient.get()
                .uri("/users")
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });
    }
}
