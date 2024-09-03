package com.infinbank.rest.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.parameters.RequestBody;
import org.springdoc.core.customizers.OpenApiCustomiser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(
                contact = @Contact(
                        name = "Will",
                        email = "batir2jumatov@gmail.com"
                ),
                description = "OpenApi documentation for Spring Boot CRUD Api",
                title = "OpenApi specification - Will",
                version = "1.0",
                license = @License(
                        name = "License name",
                        url = "https://some-url.com"
                ),
                termsOfService = "Terms of service"
        ),

        servers = {
                @Server(
                        description = "Local ENV",
                        url = "http://localhost:9090"
                ),
                @Server(
                        description = "PROD ENV",
                        url = "https://youtube.com"
                )
        }
)
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenApiCustomiser customizeOpenApi() {
        return openApi -> openApi.getPaths().values().forEach(pathItem -> pathItem.readOperations().forEach(operation -> {
            // Add default content type for request bodies
            if (operation.getRequestBody() == null) {
                operation.setRequestBody(new RequestBody().content(new Content().addMediaType(
                        "application/json", new MediaType())));
            }
        }));
    }
}
