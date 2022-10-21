package com.mateus.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringDocConfig {

    @Bean
    public OpenAPI openAPI(){
        return new OpenAPI()
                .info(new Info()
                        .title("Compass Challenge - Payment Microservice")
                        .version("v1.0.0")
                        .description("Compass Challenge: Challenge designed to test knowledge")
                ).externalDocs(new ExternalDocumentation()
                        .description("Source Code")
                        .url("https://github.com/mateusCoder/CompassChallenge"));
    }
}
