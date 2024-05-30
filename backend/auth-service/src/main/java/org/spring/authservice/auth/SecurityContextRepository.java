package org.spring.authservice.auth;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import static org.spring.authservice.utility.TokenUtility.TOKEN_PREFIX;

/**
 * This class implements the ServerSecurityContextRepository interface and provides methods for saving and loading the security context of a server web exchange.
 * The saved security context is not supported and throws an UnsupportedOperationException if called.
 * The loaded security context is obtained from the server web exchange by extracting the authentication token from the request's authorization header.
 * The authentication token is then used to authenticate the user using the provided AuthenticationManager.
 * If the authentication is successful, the SecurityContextImpl containing the authenticated user is returned.
 *
 * Note: This class requires an instance of AuthenticationManager to be provided for authentication.
 */
@Component
@AllArgsConstructor

public class SecurityContextRepository implements ServerSecurityContextRepository {


    private final AuthenticationManager authenticationManager;


    @Override
    public Mono<Void> save(ServerWebExchange exchange, SecurityContext context) {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public Mono<SecurityContext> load(ServerWebExchange serverWebExchange) {
        return Mono.just(serverWebExchange.getRequest())
                .mapNotNull(serverHttpRequest -> serverHttpRequest.getHeaders().getFirst(HttpHeaders.AUTHORIZATION))
                .filter(authenticationHeader -> authenticationHeader !=null && authenticationHeader.startsWith(TOKEN_PREFIX))
                .switchIfEmpty(Mono.empty())
                .map(authHeader -> authHeader.replace(TOKEN_PREFIX, "".trim()))
                .flatMap(authToken -> authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authToken, authToken)))
                .map(SecurityContextImpl::new);
    }
}
