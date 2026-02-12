package com.ahmete.budget_app.apikey.dto.response;

import java.time.LocalDateTime;

public record CreateApiKeyResponse(
		Long apiKeyId,
		Long userId,
		String clientName,
		String apiKey,          // RAW KEY - sadece create'de döner
		LocalDateTime createdAt
) {}