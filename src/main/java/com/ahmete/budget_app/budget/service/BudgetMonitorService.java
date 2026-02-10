package com.ahmete.budget_app.budget.service;

import com.ahmete.budget_app.alert.entity.Alert;
import com.ahmete.budget_app.alert.entity.AlertType;
import com.ahmete.budget_app.alert.repository.AlertRepository;
import com.ahmete.budget_app.budget.config.BudgetAlertProperties;
import com.ahmete.budget_app.budget.entity.Budget;
import com.ahmete.budget_app.budget.entity.BudgetPeriodType;
import com.ahmete.budget_app.budget.repository.BudgetRepository;
import com.ahmete.budget_app.expense.repository.ExpenseRepository;
import com.ahmete.budget_app.user.entity.User;
import com.ahmete.budget_app.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class BudgetMonitorService {
	
	private final BudgetRepository budgetRepository;
	private final UserRepository userRepository;
	private final ExpenseRepository expenseRepository;
	private final AlertRepository alertRepository;
	private final BudgetAlertProperties props;
	
	public BudgetMonitorService(
			BudgetRepository budgetRepository,
			UserRepository userRepository,
			ExpenseRepository expenseRepository,
			AlertRepository alertRepository,
			BudgetAlertProperties props
	) {
		this.budgetRepository = budgetRepository;
		this.userRepository = userRepository;
		this.expenseRepository = expenseRepository;
		this.alertRepository = alertRepository;
		this.props = props;
	}
	
	@Transactional
	public void evaluateAndAlert(Long userId, LocalDate expenseDate) {
		User user = userRepository.findById(userId)
		                          .orElseThrow(() -> new NoSuchElementException("User not found: " + userId));
		
		int year = expenseDate.getYear();
		int month = expenseDate.getMonthValue();
		
		Optional<Budget> budgetOpt =
				budgetRepository.findByUserAndPeriodTypeAndYearAndMonthAndDeletedFalse(
						user, BudgetPeriodType.MONTHLY, year, month
				);
		
		if (budgetOpt.isEmpty()) {
			budgetOpt = budgetRepository.findByUserAndPeriodTypeAndYearAndMonthIsNullAndDeletedFalse(
					user, BudgetPeriodType.YEARLY, year
			);
		}
		
		if (budgetOpt.isEmpty()) return;
		
		Budget budget = budgetOpt.get();
		
		LocalDate start;
		LocalDate end;
		String periodStr;
		
		if (budget.getPeriodType() == BudgetPeriodType.MONTHLY) {
			int m = budget.getMonth(); // monthly ise null değil
			start = LocalDate.of(budget.getYear(), m, 1);
			end = YearMonth.of(budget.getYear(), m).atEndOfMonth();
			periodStr = budget.getYear() + "-" + String.format("%02d", m);
		} else {
			start = LocalDate.of(budget.getYear(), 1, 1);
			end = LocalDate.of(budget.getYear(), 12, 31);
			periodStr = String.valueOf(budget.getYear());
		}
		
		BigDecimal totalSpent = expenseRepository.sumAmountByUserIdAndExpenseDateBetween(userId, start, end);
		
		BigDecimal limit = budget.getLimitAmount();
		if (limit == null || limit.compareTo(BigDecimal.ZERO) <= 0) return;
		
		BigDecimal ratio = totalSpent.divide(limit, 4, RoundingMode.HALF_UP);
		
		AlertType type = resolveType(ratio);
		if (type == null) return;
		
		boolean exists = alertRepository.existsByUserIdAndPeriodTypeAndYearAndMonthAndType(
				userId, budget.getPeriodType(), budget.getYear(), budget.getMonth(), type
		);
		if (exists) return;
		
		String message = "Budget breach: type=" + type +
				", limit=" + limit +
				", total=" + totalSpent +
				", ratio=" + ratio +
				", period=" + periodStr;
		
		Alert alert = new Alert(
				user,
				budget,
				budget.getPeriodType(),
				budget.getYear(),
				budget.getMonth(),
				limit,
				totalSpent,
				type,
				message
		);
		
		alertRepository.save(alert);
	}
	
	private AlertType resolveType(BigDecimal ratio) {
		if (ratio.compareTo(props.getWarning()) < 0) return null;
		if (ratio.compareTo(props.getExceeded()) < 0) return AlertType.WARNING;
		if (ratio.compareTo(props.getCritical()) < 0) return AlertType.BUDGET_EXCEEDED;
		return AlertType.CRITICAL;
	}
}