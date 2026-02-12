package com.ahmete.budget_app.auth.dto;

public record TokenResponse(
		Long userId,
		String accessToken,
		String refreshToken
) {}