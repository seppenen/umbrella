package org.spring.authservice.service.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import io.jsonwebtoken.JwtException;
import lombok.AllArgsConstructor;
import org.spring.authservice.enums.TokenEnum;
import org.spring.authservice.service.JwtService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Date;

@Service
@AllArgsConstructor
public class JwtServiceImpl implements JwtService {
    private static final Algorithm SECRET_KEY = Algorithm.HMAC256(TokenEnum.SECRET.getAsString());


    public Mono<String> generateToken(long expireTime, String type, String email) {
        return Mono.just(JWT.create()
                .withIssuer(TokenEnum.ISSUER.getAsString())
                .withClaim("type", type )
                .withClaim("principal", email)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + expireTime))
                .sign(SECRET_KEY));
    }

    public Mono<DecodedJWT> validateToken(String token) {
        try {
            return Mono.just(JWT.require(SECRET_KEY)
                    .withIssuer(TokenEnum.ISSUER.getAsString())
                    .withClaim("type", TokenEnum.ACCESS_TOKEN_TYPE.getAsString())
                    .build()
                    .verify(token));
        } catch (RuntimeException e) {
            return Mono.error(() -> new JwtException("Token Verification Failed ", e) {
            });
        }
    }

}
