package com.ahmete.budget_app.common.security;

import java.io.Serializable;

/**
 * Hem API Key hem JWT için tek principal.
 * - userId: her zaman dolu
 * - apiKeyId: sadece API Key ile dolu (JWT'de null)
 * - clientName: "jwt" veya api key clientName
 */
public record ApiKeyPrincipal(
		Long userId,
		Long apiKeyId,
		String clientName
) implements Serializable { }