package com.ahmete.budget_app.budget.entity;

import com.ahmete.budget_app.common.entity.SoftDeletableEntity;
import com.ahmete.budget_app.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.math.BigDecimal;

@Entity
@Table(name = "budgets")
@SQLDelete(sql = "UPDATE budgets SET deleted = true, deleted_at = CURRENT_TIMESTAMP WHERE id = ?")
@SQLRestriction("deleted = false")
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
    
    @Column(nullable = false)
    private boolean active = true;
    
    protected Budget() { }
    
    public Budget(User user, BudgetPeriodType periodType, int year, Integer month, BigDecimal limitAmount) {
        this.user = user;
        this.periodType = periodType;
        this.year = year;
        this.month = month;
        this.limitAmount = limitAmount;
        this.active = true;
        validatePeriod();
    }
    
    public void changeLimit(BigDecimal newLimit) {
        if (newLimit == null || newLimit.signum() <= 0) {
            throw new IllegalArgumentException("limitAmount must be > 0");
        }
        this.limitAmount = newLimit;
    }
    
    public void activate() {
        this.active = true;
    }
    
    public void deactivate() {
        this.active = false;
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