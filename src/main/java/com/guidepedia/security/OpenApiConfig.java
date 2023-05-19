package com.guidepedia.security;

import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI openApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Guidepedia API")
                        .description("")
                        .version("v1.0")
                        .contact(new Contact()
                                .name("Name")
                                .url("TG")
                                .email("EMAIL@mail.ru"))
                );
    }
}