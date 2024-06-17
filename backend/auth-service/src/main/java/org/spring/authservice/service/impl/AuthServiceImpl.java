package org.spring.authservice.service.impl;

import lombok.AllArgsConstructor;
import org.spring.authservice.client.UserServiceClient;
import org.spring.authservice.dto.UserCredentialDto;
import org.spring.authservice.dto.UserEntityDto;
import org.spring.authservice.entity.TokenStateEntity;
import org.spring.authservice.repository.RefreshTokenRepository;
import org.spring.authservice.service.AuthService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {

    private UserServiceClient userServiceClient;
    private RefreshTokenRepository refreshTokenRepository;

    /**
     * Requests the authentication of a user.
     *
     * @param userCredentialDto The user credentials containing the email and password.
     * @return A Mono that emits the authenticated user entity.
     */
    public Mono<UserEntityDto> requestAuthenticatedUser(UserCredentialDto userCredentialDto) {
        return userServiceClient.requestUserAuthentication(userCredentialDto);
    }


    public void persistToken(TokenStateEntity tokenStateEntity) {
        refreshTokenRepository.save(tokenStateEntity);
    }

    public void evictOldTokens(TokenStateEntity tokenStateEntity) {
        refreshTokenRepository.deleteAll(refreshTokenRepository.findByEmailAndTokenNot(tokenStateEntity.getEmail(), tokenStateEntity.getToken()));
    }

    public Optional<TokenStateEntity> findRefreshToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }
}
