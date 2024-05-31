package org.spring.authservice.service;

import org.spring.authservice.dto.UserCredentialDto;
import org.spring.authservice.dto.UserEntityDto;
import org.spring.authservice.entity.AuthenticationTokenData;
import reactor.core.publisher.Mono;

import java.util.Optional;

public interface AuthService {

     void updateToken(AuthenticationTokenData tokenStateEntity);
     Mono<UserEntityDto> requestAuthenticatedUser(UserCredentialDto userCredentialDto);
     Optional<AuthenticationTokenData> findRefreshToken(String token);
}
