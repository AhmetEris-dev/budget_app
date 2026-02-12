package com.ahmete.budget_app.budget.dto.request;

import com.ahmete.budget_app.budget.entity.BudgetPeriodType;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record UpsertBudgetRequest(
		@NotNull BudgetPeriodType periodType,
		@Min(2000) int year,
		@Min(1) @Max(12) Integer month,
		@NotNull @Positive BigDecimal limitAmount
) {
}