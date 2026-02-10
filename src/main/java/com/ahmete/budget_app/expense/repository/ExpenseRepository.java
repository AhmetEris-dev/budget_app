package com.ahmete.budget_app.expense.repository;

import com.ahmete.budget_app.expense.entity.Expense;
import com.ahmete.budget_app.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    
    @Query(value = """
        SELECT COALESCE(SUM(amount), 0)
        FROM expenses
        WHERE user_id = :userId
          AND expense_date BETWEEN :start AND :end
        """, nativeQuery = true)
    BigDecimal sumAmountByUserIdAndExpenseDateBetween(
            @Param("userId") Long userId,
            @Param("start") LocalDate start,
            @Param("end") LocalDate end
    );
    
    List<Expense> findByUserAndExpenseDateBetweenOrderByExpenseDateAsc(
            User user,
            LocalDate start,
            LocalDate end
    );
}