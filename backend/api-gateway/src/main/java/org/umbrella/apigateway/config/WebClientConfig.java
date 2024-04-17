package org.umbrella.apigateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Value("${user.server.url}")
    String authServerUrl;


    private static final String PREFIX = "/api/v1";

    @LoadBalanced
    @Bean
    public WebClient userServiceWebClient() {
        return WebClient.builder()
                .baseUrl(authServerUrl + PREFIX)
                .build();
    }
}
