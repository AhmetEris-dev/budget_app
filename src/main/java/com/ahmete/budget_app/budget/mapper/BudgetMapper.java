package com.ahmete.budget_app.budget.mapper;

import com.ahmete.budget_app.budget.dto.response.BudgetResponse;
import com.ahmete.budget_app.budget.entity.Budget;

public final class BudgetMapper {
	
	private BudgetMapper() {}
	
	public static BudgetResponse toResponse(Budget budget) {
		return new BudgetResponse(
				budget.getId(),
				budget.getPeriodType(),
				budget.getYear(),
				budget.getMonth(),
				budget.getLimitAmount(),
				budget.getCreatedAt()
		);
		
	}
}