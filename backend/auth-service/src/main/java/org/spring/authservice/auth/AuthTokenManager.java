package org.spring.authservice.auth;

import lombok.AllArgsConstructor;
import org.spring.authservice.entity.AccessTokenEntity;
import org.spring.authservice.entity.TokenStateEntity;
import org.spring.authservice.repository.AccessTokenRepository;
import org.spring.authservice.repository.RefreshTokenRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class AuthTokenManager {

    private AccessTokenRepository accessTokenRepository;
    private RefreshTokenRepository refreshTokenRepository;

    public void saveToken( AccessTokenEntity token) {
        accessTokenRepository.save(token);
    }

    public Optional<AccessTokenEntity> getToken(Long id) {
        return accessTokenRepository.findById(id);
    }

    public void persistAccessToken(AccessTokenEntity accessTokenEntity){
        accessTokenRepository.save(accessTokenEntity);
    }


    public void persistRefreshToken(TokenStateEntity tokenStateEntity) {
        refreshTokenRepository.save(tokenStateEntity);
    }

    public void evictOldRefreshTokens(TokenStateEntity tokenStateEntity) {
        refreshTokenRepository.deleteAll(refreshTokenRepository.findByEmailAndTokenNot(tokenStateEntity.getEmail(), tokenStateEntity.getToken()));
    }

    public Optional<TokenStateEntity> findRefreshToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }
}
