package com.ahmete.budget_app.expense.dto.request;

import com.ahmete.budget_app.expense.entity.ExpenseType;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CreateExpenseRequest(
		@NotNull Long userId,
		
		@NotNull
		@DecimalMin(value = "0.01", message = "amount must be > 0")
		@Digits(integer = 17, fraction = 2)
		BigDecimal amount,
		
		@NotNull LocalDate expenseDate,
		
		@NotBlank
		@Size(max = 255)
		String title,
		
		@Size(max = 1000)
		String description,
		
		@NotNull ExpenseType type
) {}