package com.ahmete.budget_app.budget.dto.request;

import com.ahmete.budget_app.budget.entity.BudgetPeriodType;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record UpsertBudgetRequest(
		@NotNull BudgetPeriodType periodType,
		@Min(2000) int year,
		Integer month,
		@NotNull @Positive BigDecimal limitAmount
) {}