package org.umbrella.configuration;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "service")
@Getter
public class ServiceProperties {
    private String authServerUrl;
    private String userServiceUrl;

}
