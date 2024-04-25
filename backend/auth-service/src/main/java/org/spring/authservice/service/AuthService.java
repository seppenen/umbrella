package org.spring.authservice.service;

import org.spring.authservice.client.UserServiceClient;
import org.spring.authservice.dto.UserCredentialDto;
import org.spring.authservice.dto.UserEntityDto;
import org.spring.authservice.entity.TokenState;
import org.spring.authservice.repository.TokenRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class AuthService {

    private final UserServiceClient userServiceClient;
    private final TokenRepository tokenRepository;

    public AuthService(UserServiceClient userServiceClient, TokenRepository tokenRepository) {
        this.userServiceClient = userServiceClient;
        this.tokenRepository = tokenRepository;
    }

    public Mono<UserEntityDto> requestAuthenticatedUser(UserCredentialDto userCredentialDto) {
        return userServiceClient.requestUserAuthentication(userCredentialDto);
    }

    public void saveToken(String token, String username) {
        tokenRepository.save(new TokenState(null, token, true, username));
    }

}
