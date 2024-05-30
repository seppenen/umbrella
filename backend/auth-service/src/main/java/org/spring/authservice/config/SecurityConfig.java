package org.spring.authservice.config;


import lombok.RequiredArgsConstructor;
import org.spring.authservice.auth.AuthenticationManager;
import org.spring.authservice.auth.JwtAuthenticationFilter;
import org.spring.authservice.auth.SecurityContextRepository;
import org.spring.authservice.exceptionHandlers.DelegatedServerAuthenticationEntryPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final AuthenticationManager authenticationManager;
    private final SecurityContextRepository securityContextRepository;

    @Bean
    public SecurityWebFilterChain securityFilterChain(ServerHttpSecurity http) {

        http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authenticationManager(authenticationManager)
                .securityContextRepository(securityContextRepository)
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(new DelegatedServerAuthenticationEntryPoint()))
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers(
                                "swagger-ui/**",
                                "v3/api-docs/**",
                                "api/v1/health/**",
                                "api/v1/authenticate/**"
                        )
                        .permitAll())
                        .authorizeExchange(exchanges -> exchanges
                                .anyExchange().authenticated()
                                );

        return http.build();
    }



}
