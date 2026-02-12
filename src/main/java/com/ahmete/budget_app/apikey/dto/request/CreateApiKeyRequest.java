package com.ahmete.budget_app.apikey.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateApiKeyRequest(
		@NotNull Long userId,
		@NotBlank String clientName
) {}