package com.ahmete.budget_app.auth.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "security.jwt")
public record JwtProperties(
		String issuer,
		String secret,
		int accessTokenMinutes,
		int refreshTokenDays
) {}