package com.ahmete.budget_app.auth.dto;

public record TokenResponse(
		String accessToken,
		String refreshToken
) {}