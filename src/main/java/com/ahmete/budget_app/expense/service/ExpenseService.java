package com.ahmete.budget_app.expense.service;

import com.ahmete.budget_app.budget.service.BudgetMonitorService;
import com.ahmete.budget_app.expense.dto.request.CreateExpenseRequest;
import com.ahmete.budget_app.expense.dto.response.ExpenseResponse;
import com.ahmete.budget_app.expense.dto.response.ExpenseSummaryResponse;
import com.ahmete.budget_app.expense.entity.Expense;
import com.ahmete.budget_app.expense.repository.ExpenseRepository;
import com.ahmete.budget_app.user.entity.User;
import com.ahmete.budget_app.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ExpenseService {
    
    private final ExpenseRepository expenseRepository;
    private final UserRepository userRepository;
    private final BudgetMonitorService budgetMonitorService;
    
    public ExpenseService(
            ExpenseRepository expenseRepository,
            UserRepository userRepository,
            BudgetMonitorService budgetMonitorService
    ) {
        this.expenseRepository = expenseRepository;
        this.userRepository = userRepository;
        this.budgetMonitorService = budgetMonitorService;
    }
    
    @Transactional
    public ExpenseResponse create(CreateExpenseRequest request) {
        User user = userRepository.findById(request.userId())
                                  .orElseThrow(() -> new NoSuchElementException("User not found: " + request.userId()));
        
        Expense expense = new Expense(
                user,
                request.amount(),
                request.expenseDate(),
                request.title(),
                request.description(),
                request.type()
        );
        
        Expense saved = expenseRepository.save(expense);
        
        // ✅ Tek doğru yer: budget/alert kontrolü
        budgetMonitorService.evaluateAndAlert(user.getId(), saved.getExpenseDate());
        
        return toResponse(saved);
    }
    
    @Transactional(readOnly = true)
    public List<ExpenseResponse> listByPeriod(Long userId, LocalDate start, LocalDate end) {
        User user = userRepository.findById(userId)
                                  .orElseThrow(() -> new NoSuchElementException("User not found: " + userId));
        
        return expenseRepository
                .findByUserAndExpenseDateBetweenOrderByExpenseDateAsc(user, start, end)
                .stream()
                .map(this::toResponse)
                .toList();
    }
    
    @Transactional(readOnly = true)
    public ExpenseSummaryResponse sumByPeriod(Long userId, LocalDate start, LocalDate end) {
        BigDecimal total = expenseRepository.sumAmountByUserIdAndExpenseDateBetween(userId, start, end);
        if (total == null) total = BigDecimal.ZERO;
        
        return new ExpenseSummaryResponse(
                userId,
                start.toString(),
                end.toString(),
                total
        );
    }
    
    private ExpenseResponse toResponse(Expense e) {
        return new ExpenseResponse(
                e.getId(),
                e.getUser().getId(),
                e.getAmount(),
                e.getExpenseDate(),
                e.getTitle(),
                e.getDescription(),
                e.getType(),
                e.getCreatedAt()
        );
    }
}