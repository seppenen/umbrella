package org.spring.authservice.config;


import org.spring.authservice.exceptionHandlers.DelegatedServerAuthenticationEntryPoint;
import org.spring.authservice.filters.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
@EnableMethodSecurity
public class SecurityConfig {
    /**
     * The JwtAuthenticationFilter class is responsible for authenticating requests by validating JWT tokens.
     * It implements the WebFilter interface.
     *
     * This filter checks if an incoming request contains a JWT token in the Authorization header and valid signature.
     * If a valid token is found, it creates and sets an Authentication object with the token as its principal.
     * If the request does not contain a valid token, it continues the filter chain without modifying the
     * request or setting any authentication information.
     *
     * If the token validation fails, the authentication failure is handled by a delegated ServerAuthenticationEntryPoint,
     * which sets the response status to UNAUTHORIZED and returns an error response.
     */
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(
            JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    /**
     * This method configures the security filter chain for server-side HTTP security.
     *
     * @param http The ServerHttpSecurity object used to configure the security.
     * @return The configured SecurityWebFilterChain object.
     */
    @Bean
    public SecurityWebFilterChain securityFilterChain(ServerHttpSecurity http) {
        http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers(
                                "swagger-ui/**",
                                "v3/api-docs/**",
                                "api/v1/token/**",
                                "api/v1/health/**"
                        )
                        .permitAll())
                        .authorizeExchange(exchanges -> exchanges
                                .anyExchange().authenticated())
                .addFilterBefore(jwtAuthenticationFilter, SecurityWebFiltersOrder.AUTHORIZATION)
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(new DelegatedServerAuthenticationEntryPoint())
                );

        return http.build();
    }
}
