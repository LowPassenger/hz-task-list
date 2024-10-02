package com.herc.test.hztasklist.config

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.security.SecurityRequirement
import io.swagger.v3.oas.models.security.SecurityScheme
import io.swagger.v3.oas.models.servers.Server
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component

@Component
@ConditionalOnProperty(name = ["use-open-api"], havingValue = "true")
class OpenApiConfig {
    @Bean
    fun openAPI(): OpenAPI {
        return OpenAPI().addSecurityItem(SecurityRequirement().addList("Bearer Authentication"))
            .components(Components().addSecuritySchemes("Bearer Authentication", createAPIKeyScheme()))
            .addServersItem(Server().url("http://localhost:8080"))
            .addServersItem(Server().url("https://9262-79-142-197-155.ngrok-free.app"))
            .info(
                Info().title("Hz Task List API")
                    .description("Hz Task List API implemented with Spring Boot RESTful service "
                            + "and documented using springdoc-openapi and OpenAPI 3.0")
                    .version("1.0")
            )
    }

    private fun createAPIKeyScheme(): SecurityScheme? {
        return SecurityScheme().type(SecurityScheme.Type.HTTP)
            .bearerFormat("JWT")
            .scheme("bearer")
    }
}