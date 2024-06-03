package org.spring.authservice.auth;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.spring.authservice.entity.AccessTokenData;
import org.spring.authservice.entity.AuthenticationTokenData;
import org.spring.authservice.enums.TokenEnum;
import org.spring.authservice.repository.AccessTokenRepository;
import org.spring.authservice.repository.RefreshTokenRepository;
import org.spring.authservice.service.JwtService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple3;

import java.util.Optional;

@Service
@AllArgsConstructor
public class AuthTokenManager {
    private JwtService jwtService;
    private AccessTokenRepository accessTokenRepository;
    private RefreshTokenRepository refreshtokenRepository;
    private final ModelMapper mapper;

    public void saveToken( AccessTokenData token) {
        accessTokenRepository.save(token);
    }

    public Optional<AccessTokenData> getToken(Long id) {
        return accessTokenRepository.findById(id);
    }

    /**
     * Retrieves the refresh token, access token, and email for a given user.
     *
     * @param email the email of the user
     * @return a Mono emitting a Tuple3 containing the refresh token, access token, and email
     */
    public Mono<Tuple3<String, String, String>> obtainTokens(String email) {
        return Mono.zip(
                        jwtService.generateToken(
                                TokenEnum.REFRESH_TOKEN_EXPIRE_TIME.getAsInteger(),
                                TokenEnum.REFRESH_TOKEN_TYPE.getAsString(),
                                email),
                        jwtService.generateToken(
                                TokenEnum.ACCESS_TOKEN_EXPIRE_TIME.getAsInteger(),
                                TokenEnum.ACCESS_TOKEN_TYPE.getAsString(),
                                email),
                        Mono.just(email)
        );
    }

   /**
    * Permanently persists the refresh token and access token data.
    *
    * @param tokens a Tuple3 containing the refresh token, access token, and email
    * @return a Mono emitting Void
    */
   public Mono<Void> persistsTokens(Tuple3<String, String, String> tokens){
        AuthenticationTokenData tokenStateEntity = new AuthenticationTokenData(tokens.getT2(),tokens.getT3());
        refreshtokenRepository.save(tokenStateEntity);
        AccessTokenData accessTokenDataToPersist = mapper.map(tokenStateEntity, AccessTokenData.class);
        accessTokenRepository.save(accessTokenDataToPersist);
        return Mono.empty();
    }

    @Transactional
    public void updateRefreshToken(AuthenticationTokenData tokenStateEntity) {
        AuthenticationTokenData tokenData = refreshtokenRepository.save(tokenStateEntity);
        refreshtokenRepository.deleteAll(refreshtokenRepository.findByEmailAndTokenNot(tokenData.getEmail(), tokenData.getToken()));
    }


    public Optional<AuthenticationTokenData> findRefreshToken(String token) {
        return refreshtokenRepository.findByToken(token);
    }

}
