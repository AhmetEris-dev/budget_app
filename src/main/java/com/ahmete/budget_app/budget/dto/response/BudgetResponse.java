package com.ahmete.budget_app.budget.dto.response;

import com.ahmete.budget_app.budget.entity.BudgetPeriodType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record BudgetResponse(
		Long id,
		BudgetPeriodType periodType,
		int year,
		Integer month,
		BigDecimal limitAmount,
		LocalDateTime createdAt
) {}