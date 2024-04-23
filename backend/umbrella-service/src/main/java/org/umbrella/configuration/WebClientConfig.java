package org.umbrella.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    private static final String PREFIX = "/api/v1";
    private final ServiceProperties serviceProperties;

    public WebClientConfig(ServiceProperties serviceProperties) {
        this.serviceProperties = serviceProperties;
    }

    @Bean
    public WebClient authServerWebClient() {
        return WebClient.builder().baseUrl(serviceProperties.getAuthServerUrl() + PREFIX).build();
    }

    @Bean
    public WebClient userServiceWebClient() {
        return WebClient.builder().baseUrl(serviceProperties.getUserServiceUrl() + PREFIX).build();
    }

}
