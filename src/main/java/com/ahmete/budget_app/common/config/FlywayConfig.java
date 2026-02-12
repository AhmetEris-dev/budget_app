package com.ahmete.budget_app.common.config;

import org.springframework.boot.flyway.autoconfigure.FlywayConfigurationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FlywayConfig {
	
	@Bean
	public FlywayConfigurationCustomizer flywayConfigurationCustomizer() {
		return cfg -> cfg
				.baselineOnMigrate(false)
				.baselineVersion("1")
				.baselineDescription("baseline existing schema (ddl-auto -> flyway)");
	}
}