package com.ahmete.budget_app.common.security;

public record ApiKeyPrincipal(Long userId, Long apiKeyId, String clientName) {}