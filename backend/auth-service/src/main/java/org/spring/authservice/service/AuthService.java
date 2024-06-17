package org.spring.authservice.service;

import org.spring.authservice.dto.UserCredentialDto;
import org.spring.authservice.dto.UserEntityDto;
import org.spring.authservice.entity.TokenStateEntity;
import reactor.core.publisher.Mono;

import java.util.Optional;

public interface AuthService {
     Mono<UserEntityDto> requestAuthenticatedUser(UserCredentialDto userCredentialDto);

     void persistToken(TokenStateEntity tokenStateEntity);

     void evictOldTokens(TokenStateEntity tokenStateEntity);

     Optional<TokenStateEntity> findRefreshToken(String token);


}
