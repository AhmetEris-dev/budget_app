package com.ahmete.budget_app.apikey.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CreateApiKeyRequest(
		@NotBlank String clientName
) {}