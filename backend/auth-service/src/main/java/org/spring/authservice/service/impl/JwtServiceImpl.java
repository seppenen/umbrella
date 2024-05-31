package org.spring.authservice.service.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.spring.authservice.enums.TokenEnum;
import org.spring.authservice.service.JwtService;
import org.spring.authservice.service.LoggerService;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {
    private static final Algorithm SECRET_KEY = Algorithm.HMAC256(TokenEnum.SECRET.getAsString());

    private final LoggerService loggerService;

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

    public String extractToken(ServerWebExchange exchange) {
        String bearerToken = exchange.getRequest().getHeaders().getFirst("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring("Bearer ".length());
        }
        loggerService.getInfoBuilder()
                .withMessage("Token not found in request")
                .log();
        return null;
    }
}