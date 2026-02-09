package com.ahmete.budget_app.alert.repository;

import com.ahmete.budget_app.alert.entity.Alert;
import com.ahmete.budget_app.alert.entity.AlertType;
import com.ahmete.budget_app.budget.entity.BudgetPeriodType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlertRepository extends JpaRepository<Alert, Long> {
	boolean existsByUserIdAndPeriodTypeAndYearAndMonthAndType(
			Long userId,
			BudgetPeriodType periodType,
			int year,
			Integer month,
			AlertType type);
	
}