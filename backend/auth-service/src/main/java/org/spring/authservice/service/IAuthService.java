package org.spring.authservice.service;

import org.spring.authservice.dto.UserCredentialDto;
import org.spring.authservice.dto.UserEntityDto;
import reactor.core.publisher.Mono;

public interface IAuthService {
    public void saveToken(String token, String username);
    public Mono<UserEntityDto> requestAuthenticatedUser(UserCredentialDto userCredentialDto);
}
