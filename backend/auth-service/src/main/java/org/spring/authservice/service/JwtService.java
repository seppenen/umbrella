package org.spring.authservice.service;

import com.auth0.jwt.interfaces.DecodedJWT;
import reactor.core.publisher.Mono;

public interface JwtService {
     Mono<String> generateToken(long expireTime, String type, String email);
     Mono<DecodedJWT> validateToken(String token);

}
