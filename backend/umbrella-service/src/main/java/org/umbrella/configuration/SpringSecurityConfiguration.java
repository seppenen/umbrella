package org.umbrella.configuration;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.umbrella.service.ApiResponseErrorFactory;
import org.umbrella.utils.JwtAuthFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SpringSecurityConfiguration {

    private final JwtAuthFilter jwtAuthFilter;
    private final ApiResponseErrorFactory apiResponseFactory;

    private final AuthenticationEntryPoint authenticationEntryPoint;

    public SpringSecurityConfiguration(
            JwtAuthFilter jwtAuthFilter,
            ApiResponseErrorFactory apiResponseFactory,
            @Qualifier("delegatedAuthenticationEntryPoint")
            AuthenticationEntryPoint authenticationEntryPoint

    ) {
        this.jwtAuthFilter = jwtAuthFilter;
        this.apiResponseFactory = apiResponseFactory;
        this.authenticationEntryPoint = authenticationEntryPoint;
    }

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
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers(
                                "swagger-ui/**",
                                "v3/api-docs/**",
                                "api/v1/refresh-token/**"
                        )
                        .permitAll()
                        .requestMatchers("api/v1/**").authenticated()
                )

                //TODO: improve exception handling
                .addFilterAfter(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling((exceptionHandling) -> exceptionHandling
                        .authenticationEntryPoint(authenticationEntryPoint)
                        .accessDeniedHandler((request, response, accessDeniedException) ->
                                response.sendError(HttpServletResponse.SC_FORBIDDEN))
                );
        return http.build();
    }

}
