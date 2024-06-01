package org.spring.authservice.service;

import org.spring.authservice.dto.UserCredentialDto;
import org.spring.authservice.dto.UserEntityDto;
import reactor.core.publisher.Mono;

public interface AuthService {
     Mono<UserEntityDto> requestAuthenticatedUser(UserCredentialDto userCredentialDto);

}
