package com.service.userService.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Value("${auth.server.url}")
    String authServiceUrl;


    private static final String PREFIX = "/api/v1";

    @Bean
    public WebClient authServiceWebClient() {
        return WebClient.builder()
                .baseUrl(authServiceUrl + PREFIX)
                .build();
    }
}
