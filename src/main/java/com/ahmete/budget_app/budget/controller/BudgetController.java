package com.ahmete.budget_app.budget.controller;

import com.ahmete.budget_app.budget.dto.UpdateBudgetRequest;
import com.ahmete.budget_app.budget.entity.Budget;
import com.ahmete.budget_app.budget.service.BudgetService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/budgets")
public class BudgetController {
	
	private final BudgetService budgetService;
	
	public BudgetController(BudgetService budgetService) {
		this.budgetService = budgetService;
	}
	
	@PutMapping
	@ResponseStatus(HttpStatus.OK)
	public Budget upsert(@Valid @RequestBody UpdateBudgetRequest request) {
		return budgetService.updateBudget(
				request.userId(),
				request.periodType(),
				request.year(),
				request.month(),
				request.limitAmount()
		);
	}
}