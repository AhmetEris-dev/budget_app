package com.ahmete.budget_app.budget.entity;

import com.ahmete.budget_app.common.entity.SoftDeletableEntity;
import com.ahmete.budget_app.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.SQLDelete;


import java.math.BigDecimal;

/**
 * Uniqueness: UNIQUE(user_id, period_type, year, month, deleted).
 * A partial unique index (e.g. WHERE deleted = false) is preferred in PostgreSQL
 * to enforce a single active budget per (user, periodType, year, month).
 */
@Entity
@Table(name = "budgets", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "period_type", "year", "month", "deleted"})
})
@SQLDelete(sql = "UPDATE budgets SET deleted = true, deleted_at = CURRENT_TIMESTAMP WHERE id = ?")
@Getter
public class Budget extends SoftDeletableEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "period_type", nullable = false, length = 20)
    private BudgetPeriodType periodType;

    @Column(nullable = false)
    private int year;

    @Column
    private Integer month;

    @Column(name = "limit_amount", nullable = false, precision = 19, scale = 2)
    private BigDecimal limitAmount;

    protected Budget() {
    }

    public Budget(User user, BudgetPeriodType periodType, int year, Integer month, BigDecimal limitAmount) {
        this.user = user;
        this.periodType = periodType;
        this.year = year;
        this.month = month;
        this.limitAmount = limitAmount;
        validatePeriod();
    }

    @PrePersist
    @PreUpdate
    private void validatePeriod() {
        if (periodType == BudgetPeriodType.MONTHLY) {
            if (month == null || month < 1 || month > 12) {
                throw new IllegalStateException("MONTHLY budget requires month in 1-12");
            }
        } else {
            if (month != null) {
                throw new IllegalStateException("YEARLY budget must have month = null");
            }
        }
    }
}