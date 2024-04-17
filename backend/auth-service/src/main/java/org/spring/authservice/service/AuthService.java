package org.spring.authservice.service;

import org.spring.authservice.client.UserServiceClient;
import org.spring.authservice.dto.UserCredentialDto;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class AuthService {

    private final UserServiceClient userServiceClient;

    public AuthService(UserServiceClient userServiceClient) {
        this.userServiceClient = userServiceClient;
    }

    public Mono<Void> authenticate(UserCredentialDto userCredentialDto) {
        return userServiceClient.validateUserCredentials(userCredentialDto);

    }

}
