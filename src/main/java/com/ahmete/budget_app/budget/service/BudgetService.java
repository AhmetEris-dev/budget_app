package com.ahmete.budget_app.budget.service;

import com.ahmete.budget_app.budget.entity.Budget;
import com.ahmete.budget_app.budget.entity.BudgetPeriodType;
import com.ahmete.budget_app.budget.repository.BudgetRepository;
import com.ahmete.budget_app.user.entity.User;
import com.ahmete.budget_app.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.NoSuchElementException;

@Service
public class BudgetService {

    private final BudgetRepository budgetRepository;
    private final UserRepository userRepository;

    public BudgetService(BudgetRepository budgetRepository, UserRepository userRepository) {
        this.budgetRepository = budgetRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public Budget updateBudget(Long userId, BudgetPeriodType periodType, int year, Integer month, BigDecimal limitAmount) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found: " + userId));

        if (periodType == BudgetPeriodType.MONTHLY) {
            budgetRepository.findByUserAndPeriodTypeAndYearAndMonthAndDeletedFalse(user, periodType, year, month)
                    .ifPresent(existing -> {
                        existing.softDelete();
                        budgetRepository.save(existing);
                    });
        } else {
            budgetRepository.findByUserAndPeriodTypeAndYearAndMonthIsNullAndDeletedFalse(user, periodType, year)
                    .ifPresent(existing -> {
                        existing.softDelete();
                        budgetRepository.save(existing);
                    });
        }

        Budget newBudget = new Budget(user, periodType, year, month, limitAmount);
        return budgetRepository.save(newBudget);
    }
}