package com.ahmete.budget_app.expense.entity;

import com.ahmete.budget_app.common.entity.BaseEntity;
import com.ahmete.budget_app.user.entity.User;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Expense belongs to User (Domain Model B). Budget is a separate limit rule.
 * Schema: user_id (required). If migrating from budget_id, run Flyway migration:
 *   ALTER TABLE expenses DROP COLUMN IF EXISTS budget_id;
 *   ALTER TABLE expenses ADD COLUMN user_id BIGINT NOT NULL REFERENCES users(id);
 * Hibernate ddl-auto=update may add user_id but often does not drop old columns.
 */
@Entity
@Table(name = "expenses")
@Access(AccessType.FIELD)
@lombok.Getter
public class Expense extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;

    @Column(name = "expense_date", nullable = false)
    private LocalDate expenseDate;

    @Column(nullable = false, length = 255)
    private String title;

    @Column(length = 1000)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ExpenseType type;

    protected Expense() {
    }

    public Expense(User user, BigDecimal amount, LocalDate expenseDate, String title, ExpenseType type) {
        this.user = user;
        this.amount = amount;
        this.expenseDate = expenseDate;
        this.title = title;
        this.type = type;
    }

    public Expense(User user, BigDecimal amount, LocalDate expenseDate, String title, String description, ExpenseType type) {
        this.user = user;
        this.amount = amount;
        this.expenseDate = expenseDate;
        this.title = title;
        this.description = description;
        this.type = type;
    }
}