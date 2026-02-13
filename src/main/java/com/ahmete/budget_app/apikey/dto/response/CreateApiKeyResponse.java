package com.ahmete.budget_app.apikey.dto.response;

import java.time.LocalDateTime;

public record CreateApiKeyResponse(
		Long apiKeyId,
		String clientName,
		String apiKey,
		LocalDateTime createdAt
) {}