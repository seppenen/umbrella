package org.spring.authservice.service;

import org.spring.authservice.dto.UserCredentialDto;
import org.spring.authservice.dto.UserEntityDto;
import org.spring.authservice.entity.TokenStateEntity;
import reactor.core.publisher.Mono;

import java.util.Optional;

public interface IAuthService {

     void updateToken(TokenStateEntity tokenStateEntity);
     Mono<UserEntityDto> requestAuthenticatedUser(UserCredentialDto userCredentialDto);
     Optional<TokenStateEntity> findTokenByToken(String token);
}
