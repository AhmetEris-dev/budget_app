package com.ahmete.budget_app.budget.repository;

import com.ahmete.budget_app.budget.entity.Budget;
import com.ahmete.budget_app.budget.entity.BudgetPeriodType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BudgetRepository extends JpaRepository<Budget, Long> {
    
    Optional<Budget> findByUser_IdAndPeriodTypeAndYearAndMonthAndDeletedFalse(
            Long userId,
            BudgetPeriodType periodType,
            int year,
            Integer month
    );
    
    Optional<Budget> findByUser_IdAndPeriodTypeAndYearAndMonthIsNullAndDeletedFalse(
            Long userId,
            BudgetPeriodType periodType,
            int year
    );
    
    Optional<Budget> findByUser_IdAndPeriodTypeAndYearAndMonthAndActiveTrueAndDeletedFalse(
            Long userId,
            BudgetPeriodType periodType,
            int year,
            Integer month
    );
    
    Optional<Budget> findByIdAndDeletedFalse(Long id);
    
    List<Budget> findByUser_IdAndDeletedFalseOrderByCreatedAtDesc(Long userId);
}