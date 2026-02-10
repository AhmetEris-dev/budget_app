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
    
    @Query("""
        select coalesce(sum(e.amount), 0)
        from Expense e
        where e.user.id = :userId
          and e.expenseDate between :start and :end
    """)
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