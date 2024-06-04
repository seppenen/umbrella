package org.spring.authservice.service;

import org.spring.authservice.dto.UserCredentialDto;
import org.spring.authservice.dto.UserEntityDto;
import org.spring.authservice.entity.AccessTokenEntity;
import org.spring.authservice.entity.TokenStateEntity;
import reactor.core.publisher.Mono;

import java.util.Optional;

public interface AuthService {
     Mono<UserEntityDto> requestAuthenticatedUser(UserCredentialDto userCredentialDto);

     Optional<AccessTokenEntity> getToken(Long id);

     void persistAccessToken(AccessTokenEntity accessTokenEntity);

     void persistRefreshToken(TokenStateEntity tokenStateEntity);

     void evictOldRefreshTokens(TokenStateEntity tokenStateEntity);

     Optional<TokenStateEntity> findRefreshToken(String token);


}
