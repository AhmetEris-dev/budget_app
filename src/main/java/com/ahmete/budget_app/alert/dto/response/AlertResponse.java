package com.ahmete.budget_app.alert.dto.response;

import com.ahmete.budget_app.alert.entity.AlertType;
import com.ahmete.budget_app.budget.entity.BudgetPeriodType;

import java.math.BigDecimal;

public record AlertResponse(
		Long id,
		Long userId,
		Long budgetId,
		BudgetPeriodType periodType,
		int year,
		Integer month,
		BigDecimal limitAmount,
		BigDecimal totalExpense,
		AlertType type,
		String message
) {}