package com.ahmete.budget_app.alert.repository;

import com.ahmete.budget_app.alert.entity.Alert;
import com.ahmete.budget_app.alert.entity.AlertStatus;
import com.ahmete.budget_app.alert.entity.AlertType;
import com.ahmete.budget_app.budget.entity.BudgetPeriodType;
import com.ahmete.budget_app.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AlertRepository extends JpaRepository<Alert, Long> {
	
	boolean existsByUserIdAndPeriodTypeAndYearAndMonthAndType(
			Long userId,
			BudgetPeriodType periodType,
			int year,
			Integer month,
			AlertType type
	);
	Page<Alert> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);
	
	Page<Alert> findByUserIdAndStatusOrderByCreatedAtDesc(Long userId, AlertStatus status, Pageable pageable);
	
	List<Alert> findByUserOrderByCreatedAtDesc(User user);
	
	// ✅ NEW
	List<Alert> findByUserAndStatusOrderByCreatedAtDesc(User user, AlertStatus status);
	
	// ✅ NEW: user ownership check
	Optional<Alert> findByIdAndUserId(Long id, Long userId);
}