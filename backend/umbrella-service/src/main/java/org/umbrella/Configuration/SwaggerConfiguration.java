package org.umbrella.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfiguration {

    @Bean
    public OpenAPI customOpenAPI() {
        final String securitySchemeName = "BearerAuth";
        final String serviceName = "Umbrella Service";
        final String version = "v1.0";

        final String schemeType = "Bearer";
        final String bearerFormat = "JWT";

        Info info = new Info().title(serviceName)
                .version(version);


        SecurityRequirement securityRequirement = new SecurityRequirement().addList(securitySchemeName);

        SecurityScheme securityScheme = new SecurityScheme()
                .name(securitySchemeName)
                .type(SecurityScheme.Type.HTTP)
                .scheme(schemeType)
                .bearerFormat(bearerFormat);

        Components components = new Components().addSecuritySchemes(securitySchemeName, securityScheme);

        return new OpenAPI()
                .info(info)
                .addSecurityItem(securityRequirement)
                .components(components);
    }

}
