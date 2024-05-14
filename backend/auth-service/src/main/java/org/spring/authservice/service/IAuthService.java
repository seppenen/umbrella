package org.spring.authservice.service;

import org.spring.authservice.dto.UserCredentialDto;
import org.spring.authservice.dto.UserEntityDto;
import reactor.core.publisher.Mono;

public interface IAuthService {

     void persistToken(String token, String username);
     Mono<UserEntityDto> requestAuthenticatedUser(UserCredentialDto userCredentialDto);
}
