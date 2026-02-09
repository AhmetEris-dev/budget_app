package com.ahmete.budget_app.expense.service;

import com.ahmete.budget_app.alert.entity.Alert;
import com.ahmete.budget_app.alert.entity.AlertType;
import com.ahmete.budget_app.alert.repository.AlertRepository;
import com.ahmete.budget_app.budget.entity.Budget;
import com.ahmete.budget_app.budget.entity.BudgetPeriodType;
import com.ahmete.budget_app.budget.repository.BudgetRepository;
import com.ahmete.budget_app.expense.entity.Expense;
import com.ahmete.budget_app.expense.entity.ExpenseType;
import com.ahmete.budget_app.expense.repository.ExpenseRepository;
import com.ahmete.budget_app.user.entity.User;
import com.ahmete.budget_app.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final UserRepository userRepository;
    private final BudgetRepository budgetRepository;
    private final AlertRepository alertRepository;

    public ExpenseService(ExpenseRepository expenseRepository,
                          UserRepository userRepository,
                          BudgetRepository budgetRepository,
                          AlertRepository alertRepository) {
        this.expenseRepository = expenseRepository;
        this.userRepository = userRepository;
        this.budgetRepository = budgetRepository;
        this.alertRepository = alertRepository;
    }

    @Transactional
    public Expense createExpense(Long userId, BigDecimal amount, LocalDate expenseDate, String title, String description, ExpenseType type) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found: " + userId));

        Expense expense = new Expense(user, amount, expenseDate, title, description, type);
        expense = expenseRepository.save(expense);

        int year = expenseDate.getYear();
        int month = expenseDate.getMonthValue();

        Optional<Budget> budgetOpt = budgetRepository.findByUserAndPeriodTypeAndYearAndMonthAndDeletedFalse(user, BudgetPeriodType.MONTHLY, year, month);
        if (budgetOpt.isEmpty()) {
            budgetOpt = budgetRepository.findByUserAndPeriodTypeAndYearAndMonthIsNullAndDeletedFalse(user, BudgetPeriodType.YEARLY, year);
        }

        if (budgetOpt.isEmpty()) {
            return expense;
        }

        Budget budget = budgetOpt.get();
        LocalDate start;
        LocalDate end;
        String periodStr;
        int budgetYear = budget.getYear();
        Integer monthVal = budget.getMonth();

        if (budget.getPeriodType() == BudgetPeriodType.MONTHLY && monthVal != null) {
            start = LocalDate.of(budgetYear, monthVal, 1);
            end = YearMonth.of(budgetYear, monthVal).atEndOfMonth();
            periodStr = budgetYear + "-" + String.format("%02d", monthVal);
        } else {
            start = LocalDate.of(budgetYear, 1, 1);
            end = LocalDate.of(budgetYear, 12, 31);
            periodStr = String.valueOf(budgetYear);
        }
        
        BigDecimal totalSpent = expenseRepository.sumAmountByUserIdAndExpenseDateBetween(user.getId(), start, end);
        
        if (totalSpent == null) {
            totalSpent = BigDecimal.ZERO;
        }

        if (totalSpent.compareTo(budget.getLimitAmount()) > 0) {
            String message = "Budget exceeded: limit=" + budget.getLimitAmount() + ", total=" + totalSpent + ", period=" + periodStr;
            Alert alert = new Alert(user, budget, budget.getPeriodType(), budget.getYear(), budget.getMonth(),
                    budget.getLimitAmount(), totalSpent, AlertType.BUDGET_EXCEEDED, message);
            alertRepository.save(alert);
        }

        return expense;
    }

    public BigDecimal getTotalExpenseForPeriod(User user, LocalDate start, LocalDate end) {
        BigDecimal sum = expenseRepository.sumAmountByUserIdAndExpenseDateBetween(user.getId(), start, end);
        return sum != null ? sum : BigDecimal.ZERO;
    }
}