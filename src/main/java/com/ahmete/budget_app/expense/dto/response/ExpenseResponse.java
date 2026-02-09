package com.ahmete.budget_app.expense.dto.response;

import com.ahmete.budget_app.expense.entity.ExpenseType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record ExpenseResponse(
		Long id,
		Long userId,
		BigDecimal amount,
		LocalDate expenseDate,
		String title,
		String description,
		ExpenseType type,
		LocalDateTime createdAt
) {}