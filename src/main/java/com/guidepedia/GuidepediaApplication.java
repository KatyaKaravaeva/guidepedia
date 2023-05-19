package com.guidepedia;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import static org.springframework.security.config.Elements.JWT;

@SpringBootApplication
@EnableJpaRepositories("com.guidepedia")
@Configuration
@EnableCaching
@EntityScan("com.guidepedia")
@SecurityScheme(name = "Authorization", scheme = "bearer", type = SecuritySchemeType.HTTP, in = SecuritySchemeIn.HEADER, bearerFormat = JWT)
public class GuidepediaApplication {
	public static void main(String[] args) {

		SpringApplication.run(GuidepediaApplication.class, args);
	}

}
