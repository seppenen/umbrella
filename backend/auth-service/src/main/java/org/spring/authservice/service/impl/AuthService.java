package org.spring.authservice.service.impl;

import org.spring.authservice.client.UserServiceClient;
import org.spring.authservice.dto.UserCredentialDto;
import org.spring.authservice.dto.UserEntityDto;
import org.spring.authservice.entity.TokenStateEntity;
import org.spring.authservice.repository.TokenRepository;
import org.spring.authservice.service.IAuthService;
import org.spring.authservice.service.ILoggerService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
public class AuthService implements IAuthService {

    private final UserServiceClient userServiceClient;
    private final ILoggerService loggerService;
    private final TokenRepository tokenRepository;

    public AuthService(UserServiceClient userServiceClient, ILoggerService loggerService, TokenRepository tokenRepository) {
        this.userServiceClient = userServiceClient;
        this.loggerService = loggerService;
        this.tokenRepository = tokenRepository;
    }

    public Mono<UserEntityDto> requestAuthenticatedUser(UserCredentialDto userCredentialDto) {
        return userServiceClient.requestUserAuthentication(userCredentialDto);
    }


    @Transactional
    public void persistToken(TokenStateEntity tokenStateEntity) {
        TokenStateEntity persistedTokenStateEntity = tokenRepository.save(tokenStateEntity);
        deleteAllButLatestToken(persistedTokenStateEntity);
        loggerService.getInfoBuilder()
                .withMessage("Token persisted")
                .log();

    }

    public void deleteAllButLatestToken(TokenStateEntity persistedTokenStateEntity) {
        String email = persistedTokenStateEntity.getEmail();
        Long id = persistedTokenStateEntity.getId();
        tokenRepository.deleteAll(tokenRepository.findByEmailAndIdNot(email, id));
        loggerService.getInfoBuilder()
                .withMessage("Previous token deleted")
                .withData(email)
                .log();
    }

}
