package com.ahmete.budget_app.budget.controller;

import com.ahmete.budget_app.constants.RestApis;
import com.ahmete.budget_app.budget.dto.request.UpsertBudgetRequest;
import com.ahmete.budget_app.budget.dto.response.BudgetResponse;
import com.ahmete.budget_app.budget.entity.BudgetPeriodType;
import com.ahmete.budget_app.budget.service.BudgetService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(RestApis.Budget.ROOT)
public class BudgetController {
	
	private final BudgetService budgetService;
	
	public BudgetController(BudgetService budgetService) {
		this.budgetService = budgetService;
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public BudgetResponse upsert(
			@RequestParam Long userId,
			@Valid @RequestBody UpsertBudgetRequest request
	) {
		return budgetService.upsert(userId, request);
	}

	
	@GetMapping(RestApis.Budget.ACTIVE)
	public BudgetResponse getActive(
			@RequestParam Long userId,
			@RequestParam BudgetPeriodType periodType,
			@RequestParam int year,
			@RequestParam(required = false) Integer month
	) {
		return budgetService.getActive(userId, periodType, year, month);
	}
}