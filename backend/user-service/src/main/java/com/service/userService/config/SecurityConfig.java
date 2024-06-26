package com.service.userService.config;

/**
 * Author: Aleksandr Seppenen
 * Date: 05-04-2023
 * Version: 1.0
 */

import com.service.userService.filters.AuthServiceFilter;
import com.service.userService.helpers.UserDetailService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
@AllArgsConstructor
public class SecurityConfig {

    private final AuthServiceFilter authServiceFilter;
    private final UserDetailService userDetailService;

    @Bean
    public SecurityWebFilterChain securityFilterChain(ServerHttpSecurity http) {

        http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers(
                                "swagger-ui/**",
                                "v3/api-docs/**",
                                "api/v1/health/**",
                                "api/v1/login/**",
                                "api/v1/register/**"
                        )
                        .permitAll())
                .authorizeExchange(exchanges -> exchanges
                        .anyExchange().authenticated())
                .addFilterBefore(authServiceFilter, SecurityWebFiltersOrder.AUTHORIZATION);
//                .exceptionHandling(ex -> ex
//                        .authenticationEntryPoint(new DelegatedServerAuthenticationEntryPoint())
//                );


        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public ReactiveAuthenticationManager authenticationManager() {
        return new UserDetailsRepositoryReactiveAuthenticationManager(userDetailService);
    }

}
