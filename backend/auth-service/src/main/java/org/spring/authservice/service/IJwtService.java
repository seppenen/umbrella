package org.spring.authservice.service;

import org.springframework.web.server.ServerWebExchange;

public interface IJwtService {
     String generateRefreshToken();
     String generateAccessToken();
     Boolean validateToken(String token);
     String extractToken(ServerWebExchange exchange);
}
