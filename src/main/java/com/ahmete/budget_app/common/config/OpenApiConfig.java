package com.ahmete.budget_app.common.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
	
	public static final String API_KEY_SCHEME = "ApiKeyAuth";
	public static final String HEADER_NAME = "X-API-KEY";
	
	@Bean
	public OpenAPI openAPI() {
		return new OpenAPI()
				.addSecurityItem(new SecurityRequirement().addList(API_KEY_SCHEME))
				.components(new Components().addSecuritySchemes(
						API_KEY_SCHEME,
						new SecurityScheme()
								.name(HEADER_NAME)
								.type(SecurityScheme.Type.APIKEY)
								.in(SecurityScheme.In.HEADER)
				));
	}
}