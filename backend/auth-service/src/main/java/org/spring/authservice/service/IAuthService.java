package org.spring.authservice.service;

import org.spring.authservice.dto.UserCredentialDto;
import org.spring.authservice.dto.UserEntityDto;
import org.spring.authservice.entity.TokenStateEntity;
import reactor.core.publisher.Mono;

public interface IAuthService {

     void persistToken(TokenStateEntity tokenStateEntity);
     Mono<UserEntityDto> requestAuthenticatedUser(UserCredentialDto userCredentialDto);
}
