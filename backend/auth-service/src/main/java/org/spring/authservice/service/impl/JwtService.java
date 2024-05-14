package org.spring.authservice.service.impl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.spring.authservice.service.IJwtService;
import org.spring.authservice.service.ILoggerService;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ServerWebExchange;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;
import java.util.function.Function;

@Service
public class JwtService implements IJwtService {
    private static final Key SECRET_KEY = Keys.hmacShaKeyFor(Decoders.BASE64.decode(
            "357638792F423F4428472B4B6250655368566D597133743677397A2443264629"));
    private static final long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 240;
    private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 15;
    private final ILoggerService loggerService;

    public JwtService(ILoggerService loggerService) {
        this.loggerService = loggerService;
    }

    private String generateToken() {
        return Jwts.builder()
                .setIssuer("auth-service")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRE_TIME))
                .signWith(SECRET_KEY, SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateRefreshToken() {
        return generateToken();
    }

    public String generateAccessToken() {
        var claims = new HashMap<String, Object>();
        claims.put("jti", UUID.randomUUID().toString());
        return Jwts.builder()
                .setClaims(claims)
                .setIssuer("auth-service")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRE_TIME))
                .signWith(SECRET_KEY, SignatureAlgorithm.HS256)
                .compact();
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        return claimsResolver.apply(extractAllClaims(token));
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public Boolean validateToken(String token) {
        return !extractExpiration(token).before(new Date());
    }

    public String extractToken(ServerWebExchange exchange) {
        String bearerToken = exchange.getRequest().getHeaders().getFirst("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        loggerService.getInfoBuilder()
                .withMessage("Token not found in request")
                .log();
          return null;
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
