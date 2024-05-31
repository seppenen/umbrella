package org.spring.authservice.service.impl;

import lombok.AllArgsConstructor;
import org.spring.authservice.client.UserServiceClient;
import org.spring.authservice.dto.UserCredentialDto;
import org.spring.authservice.dto.UserEntityDto;
import org.spring.authservice.entity.AccessTokenData;
import org.spring.authservice.entity.AuthenticationTokenData;
import org.spring.authservice.repository.AccessTokenRepository;
import org.spring.authservice.repository.RefreshTokenRepository;
import org.spring.authservice.service.AuthService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {

    private UserServiceClient userServiceClient;
    private RefreshTokenRepository refreshtokenRepository;
    private AccessTokenRepository accessTokenRepository;

    public void saveToken( AccessTokenData token) {
        accessTokenRepository.save(token);
    }

    public Optional<AccessTokenData> getToken(Long id) {
        return accessTokenRepository.findById(id);
    }


    public Mono<UserEntityDto> requestAuthenticatedUser(UserCredentialDto userCredentialDto) {
        return userServiceClient.requestUserAuthentication(userCredentialDto);
    }

    @Transactional
    public void updateToken(AuthenticationTokenData tokenStateEntity) {
        AuthenticationTokenData persistedTokenStateEntity = refreshtokenRepository.save(tokenStateEntity);
        deleteAllButLatestToken(persistedTokenStateEntity);
    }

    public void deleteAllButLatestToken(AuthenticationTokenData persistedTokenStateEntity) {
        String email = persistedTokenStateEntity.getEmail();
        String token = persistedTokenStateEntity.getToken();
        refreshtokenRepository.deleteAll(refreshtokenRepository.findByEmailAndTokenNot(email, token));
    }

    public Optional<AuthenticationTokenData> findRefreshToken(String token) {
        return refreshtokenRepository.findByToken(token);
    }
}
