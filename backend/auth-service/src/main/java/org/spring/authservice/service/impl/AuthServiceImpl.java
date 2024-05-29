package org.spring.authservice.service.impl;

import org.spring.authservice.client.UserServiceClient;
import org.spring.authservice.dto.UserCredentialDto;
import org.spring.authservice.dto.UserEntityDto;
import org.spring.authservice.entity.TokenStateEntity;
import org.spring.authservice.repository.TokenRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Service
public class AuthServiceImpl implements org.spring.authservice.service.AuthService {
    private final UserServiceClient userServiceClient;
    private final TokenRepository tokenRepository;

    public AuthServiceImpl(UserServiceClient userServiceClient, TokenRepository tokenRepository) {
        this.userServiceClient = userServiceClient;
        this.tokenRepository = tokenRepository;
    }

    public Mono<UserEntityDto> requestAuthenticatedUser(UserCredentialDto userCredentialDto) {
        return userServiceClient.requestUserAuthentication(userCredentialDto);
    }

    @Transactional
    public void updateToken(TokenStateEntity tokenStateEntity) {
        TokenStateEntity persistedTokenStateEntity = tokenRepository.save(tokenStateEntity);
        deleteAllButLatestToken(persistedTokenStateEntity);
    }

    public void deleteAllButLatestToken(TokenStateEntity persistedTokenStateEntity) {
        String email = persistedTokenStateEntity.getEmail();
        String token = persistedTokenStateEntity.getToken();
        tokenRepository.deleteAll(tokenRepository.findByEmailAndTokenNot(email, token));
    }

    public Optional<TokenStateEntity> findRefreshToken(String token) {
        return tokenRepository.findByToken(token);
    }
}
