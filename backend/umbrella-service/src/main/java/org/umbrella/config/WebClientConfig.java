package org.umbrella.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@RequiredArgsConstructor
public class WebClientConfig {

    private static final String PREFIX = "/api/v1";
    @Value("${service.auth.url}")
    private String authServiceUrl;
    @Value("${service.user.url}")
    private String userServiceUrl;



    @Bean
    public WebClient authServerWebClient() {
        return WebClient.builder().baseUrl(authServiceUrl + PREFIX).build();
    }

    @Bean
    public WebClient userServiceWebClient() {
        return WebClient.builder().baseUrl(userServiceUrl + PREFIX).build();
    }

}
