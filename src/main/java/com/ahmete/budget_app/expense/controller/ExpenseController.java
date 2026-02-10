package com.ahmete.budget_app.expense.controller;

import com.ahmete.budget_app.constants.RestApis;
import com.ahmete.budget_app.expense.dto.request.CreateExpenseRequest;
import com.ahmete.budget_app.expense.dto.response.ExpenseResponse;
import com.ahmete.budget_app.expense.dto.response.ExpenseSummaryResponse;
import com.ahmete.budget_app.expense.service.ExpenseService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

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
		return expenseService.create(request);
	}
	
	@GetMapping
	public List<ExpenseResponse> listByPeriod(
			@RequestParam Long userId,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end
	) {
		return expenseService.listByPeriod(userId, start, end);
	}
	
	// RestApis.Expense.TOTAL = "/total" tanımlı; onu kullanıyoruz
	@GetMapping(RestApis.Expense.TOTAL)
	public ExpenseSummaryResponse summary(
			@RequestParam Long userId,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end
	) {
		return expenseService.sumByPeriod(userId, start, end);
	}
}