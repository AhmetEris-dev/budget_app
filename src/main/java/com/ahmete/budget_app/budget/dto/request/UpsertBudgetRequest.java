package com.ahmete.budget_app.budget.dto.request;

import com.ahmete.budget_app.budget.entity.BudgetPeriodType;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record UpsertBudgetRequest(
		@NotNull Long userId,
		@NotNull BudgetPeriodType periodType,
		@Min(2000) int year,
		Integer month, // MONTHLY => 1..12, YEARLY => null/absent
		@NotNull @Positive BigDecimal limitAmount
) {}