package org.umbrella;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.web.reactive.server.WebTestClient;

@Configuration
public class TestConfiguration {
    @Bean
    public WebTestClient webTestClient() {
        return WebTestClient.bindToServer().baseUrl("http://localhost/auth").build();
    }

}
