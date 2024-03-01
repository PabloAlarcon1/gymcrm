package com.gymcrm.gymcrm.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    private static final String CONTROLLERS_PACKAGE =
            "com.gymcrm.gymcrm.controller";
    private static final String TITLE = "GYM CRM System";
    private static final String DESCRIPTION = "GYM CRM System";
    private static final String VERSION = "0.0.1-SNAPSHOT";
    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group(CONTROLLERS_PACKAGE)
                .pathsToMatch("/**")
                .build();
    }

    @Bean
    public OpenAPI springShopOpenAPI() {
        return new OpenAPI()
                .info(new Info().title(TITLE)
                        .description(DESCRIPTION)
                        .version(VERSION)
                        .license(new License().name("Apache 2.0").url("http://springdoc.org")))
                .externalDocs(new ExternalDocumentation()
                        .description(DESCRIPTION)
                        .url("https://springshop.wiki.github.org/docs"));
    }
}
