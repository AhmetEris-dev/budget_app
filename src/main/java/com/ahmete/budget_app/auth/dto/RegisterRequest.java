package com.ahmete.budget_app.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
		@NotBlank @Email @Size(max=255) String email,
		@NotBlank @Size(max=255) String fullName,
		@NotBlank @Size(min=6, max=72) String password
) {}