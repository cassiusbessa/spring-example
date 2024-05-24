package com.springboot.example.restful.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@Configuration
public class OpenApiConfig {

    @Bean
    OpenAPI customOpenAPI() {
        return new OpenAPI()
            .info(new Info().title("RESTful API with Spring Boot 2.5.4")
                .version("v1")
                .description("This is a sample RESTful API with Spring Boot 2.5.4")
                .termsOfService("http://swagger.io/terms/")
                .license(new License().name("Apache 2.0").url("http://springdoc.org"))
                .contact(new io.swagger.v3.oas.models.info.Contact()
                    .email("cassiusbessa@gmail.com")
                    .name("Cassius Bessa")
                    .url("linkedin.com/in/cassius-bessa"))
                );
    }


}
