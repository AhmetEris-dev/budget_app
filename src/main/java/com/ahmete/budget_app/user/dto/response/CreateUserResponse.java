package com.ahmete.budget_app.user.dto.response;

import java.time.LocalDateTime;

public record CreateUserResponse(
		Long id,
		String email,
		String fullName,
		String apiKey,          // sadece create'te döner
		LocalDateTime createdAt
) {}