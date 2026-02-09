package com.ahmete.budget_app.expense.dto.response;

import java.math.BigDecimal;

public record ExpenseSummaryResponse(
		Long userId,
		String start,
		String end,
		BigDecimal total
) {}