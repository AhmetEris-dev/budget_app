package com.ahmete.budget_app.alert.entity;

import com.ahmete.budget_app.budget.entity.Budget;
import com.ahmete.budget_app.budget.entity.BudgetPeriodType;
import com.ahmete.budget_app.common.entity.BaseEntity;
import com.ahmete.budget_app.user.entity.User;
import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "alerts")
@Access(AccessType.FIELD)
@lombok.Getter
public class Alert extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "budget_id", nullable = false)
    private Budget budget;

    @Enumerated(EnumType.STRING)
    @Column(name = "period_type", nullable = false, length = 20)
    private BudgetPeriodType periodType;

    @Column(nullable = false)
    private int year;

    @Column
    private Integer month;

    @Column(name = "limit_amount", nullable = false, precision = 19, scale = 2)
    private BigDecimal limitAmount;

    @Column(name = "total_expense", nullable = false, precision = 19, scale = 2)
    private BigDecimal totalExpense;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private AlertType type;

    @Column(length = 500)
    private String message;

    protected Alert() {
    }

    public Alert(User user, Budget budget, BudgetPeriodType periodType, int year, Integer month,
                 BigDecimal limitAmount, BigDecimal totalExpense, AlertType type) {
        this.user = user;
        this.budget = budget;
        this.periodType = periodType;
        this.year = year;
        this.month = month;
        this.limitAmount = limitAmount;
        this.totalExpense = totalExpense;
        this.type = type;
    }

    public Alert(User user, Budget budget, BudgetPeriodType periodType, int year, Integer month,
                 BigDecimal limitAmount, BigDecimal totalExpense, AlertType type, String message) {
        this.user = user;
        this.budget = budget;
        this.periodType = periodType;
        this.year = year;
        this.month = month;
        this.limitAmount = limitAmount;
        this.totalExpense = totalExpense;
        this.type = type;
        this.message = message;
    }
}