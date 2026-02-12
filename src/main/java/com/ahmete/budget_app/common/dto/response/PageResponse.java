package com.ahmete.budget_app.common.dto.response;

import java.util.List;

public record PageResponse<T>(
		List<T> items,
		int page,
		int size,
		long totalElements,
		int totalPages
) {}