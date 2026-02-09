package com.ahmete.budget_app.budget.repository;

import com.ahmete.budget_app.budget.entity.Budget;
import com.ahmete.budget_app.budget.entity.BudgetPeriodType;
import com.ahmete.budget_app.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BudgetRepository extends JpaRepository<Budget, Long> {

    Optional<Budget> findByUserAndPeriodTypeAndYearAndMonthAndDeletedFalse(
            User user,
            BudgetPeriodType periodType,
            int year,
            Integer month);

    Optional<Budget> findByUserAndPeriodTypeAndYearAndMonthIsNullAndDeletedFalse(
            User user,
            BudgetPeriodType periodType,
            int year);
}