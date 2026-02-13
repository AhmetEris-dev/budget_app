package com.ahmete.budget_app.expense.controller;

import com.ahmete.budget_app.constants.RestApis;
import com.ahmete.budget_app.common.security.SecurityUtils;
import com.ahmete.budget_app.expense.dto.request.CreateExpenseRequest;
import com.ahmete.budget_app.expense.dto.response.ExpenseResponse;
import com.ahmete.budget_app.expense.dto.response.ExpenseSummaryResponse;
import com.ahmete.budget_app.expense.service.ExpenseService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;


@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping(RestApis.Expense.ROOT)
public class ExpenseController {
	
	private final ExpenseService expenseService;
	
	public ExpenseController(ExpenseService expenseService) {
		this.expenseService = expenseService;
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ExpenseResponse create(@Valid @RequestBody CreateExpenseRequest request) {
		Long userId = SecurityUtils.requireUserId();
		
		return expenseService.create(userId, request);
	}
	
	@GetMapping
	public List<ExpenseResponse> listByPeriod(
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end
	) {
		Long userId = SecurityUtils.requireUserId();
		return expenseService.listByPeriod(userId, start, end);
	}
	
	@GetMapping(RestApis.Expense.TOTAL)
	public ExpenseSummaryResponse summary(
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end
	) {
		Long userId = SecurityUtils.requireUserId();
		return expenseService.sumByPeriod(userId, start, end);
	}
}