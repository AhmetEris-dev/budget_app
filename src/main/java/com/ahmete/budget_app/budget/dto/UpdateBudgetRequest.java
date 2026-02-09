package com.ahmete.budget_app.budget.dto;

import com.ahmete.budget_app.budget.entity.BudgetPeriodType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record UpdateBudgetRequest(
		@NotNull Long userId,
		@NotNull BudgetPeriodType periodType,
		@Min(2000) int year,
		@Min(1) @Max(12) Integer month, // YEARLY için null gönder
		@NotNull @DecimalMin("0.01") BigDecimal limitAmount
) { }