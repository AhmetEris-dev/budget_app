package com.ahmete.budget_app.expense.dto.response;

import java.math.BigDecimal;

public record ExpenseSummaryResponse(
		String start,
		String end,
		BigDecimal total
) {}