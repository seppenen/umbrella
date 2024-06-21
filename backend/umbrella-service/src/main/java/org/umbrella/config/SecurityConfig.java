package org.umbrella.config;

import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.umbrella.exceptionHandlers.DelegatedAuthenticationEntryPoint;
import org.umbrella.filters.AuthServiceFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@AllArgsConstructor
public class SecurityConfig {

    @Qualifier("delegatedAuthenticationEntryPoint")
    private DelegatedAuthenticationEntryPoint authenticationEntryPoint;
    private AuthServiceFilter authServiceFilter;

    /**
     * Configures and returns a SecurityFilterChain for the given HttpSecurity.
     *
     * @param http the HttpSecurity object to configure
     * @return the configured SecurityFilterChain
     * @throws Exception if an error occurs during configuration
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
               // .authenticationManager(authenticationProvider)
               // .authenticationProvider(tokenAuthenticationProvider)
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers("swagger-ui.html","/api/v1/health", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        .requestMatchers("/api/v1/**").authenticated()
                )
                .addFilterBefore(authServiceFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling((exceptionHandling) -> exceptionHandling
                        .authenticationEntryPoint(authenticationEntryPoint)
                        .accessDeniedHandler((request, response, accessDeniedException) ->
                                response.sendError(HttpServletResponse.SC_FORBIDDEN))
                );
        return http.build();
    }
}
