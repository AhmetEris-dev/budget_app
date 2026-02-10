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
import java.time.YearMonth;
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
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end
	) {
		LocalDate[] range = resolveDateRange(start, end);
		return expenseService.listByPeriod(userId, range[0], range[1]);
	}
	
	@GetMapping(RestApis.Expense.TOTAL)
	public ExpenseSummaryResponse summary(
			@RequestParam Long userId,
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end
	) {
		LocalDate[] range = resolveDateRange(start, end);
		return expenseService.sumByPeriod(userId, range[0], range[1]);
	}
	
	private LocalDate[] resolveDateRange(LocalDate start, LocalDate end) {
		if (start != null && end != null) {
			return new LocalDate[]{start, end};
		}
		YearMonth ym = YearMonth.now();
		return new LocalDate[]{ym.atDay(1), ym.atEndOfMonth()};
	}
}