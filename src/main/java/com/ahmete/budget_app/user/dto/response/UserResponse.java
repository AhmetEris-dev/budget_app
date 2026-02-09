package com.ahmete.budget_app.user.dto.response;

public record UserResponse(
		Long id,
		String email,
		String fullName
) {}