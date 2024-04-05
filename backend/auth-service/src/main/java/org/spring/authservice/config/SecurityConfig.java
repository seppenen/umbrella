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
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final DelegatedServerAuthenticationEntryPoint delegatedServerAuthenticationEntryPoint;

    public SecurityConfig(
            JwtAuthenticationFilter jwtAuthenticationFilter,
            DelegatedServerAuthenticationEntryPoint delegatedServerAuthenticationEntryPoint

    ) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.delegatedServerAuthenticationEntryPoint = delegatedServerAuthenticationEntryPoint;
    }

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
                        .authenticationEntryPoint(delegatedServerAuthenticationEntryPoint)
                );

        return http.build();
    }
}
