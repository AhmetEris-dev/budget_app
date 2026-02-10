package com.ahmete.budget_app.alert.service;

import com.ahmete.budget_app.alert.dto.response.AlertResponse;
import com.ahmete.budget_app.alert.entity.Alert;
import com.ahmete.budget_app.alert.entity.AlertStatus;
import com.ahmete.budget_app.alert.repository.AlertRepository;
import com.ahmete.budget_app.user.entity.User;
import com.ahmete.budget_app.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class AlertService {
	
	private final AlertRepository alertRepository;
	private final UserRepository userRepository;
	
	public AlertService(AlertRepository alertRepository, UserRepository userRepository) {
		this.alertRepository = alertRepository;
		this.userRepository = userRepository;
	}
	
	@Transactional(readOnly = true)
	public List<AlertResponse> listByUser(Long userId, AlertStatus status) {
		User user = userRepository.findById(userId)
		                          .orElseThrow(() -> new NoSuchElementException("User not found: " + userId));
		
		List<Alert> alerts = (status == null)
				? alertRepository.findByUserOrderByCreatedAtDesc(user)
				: alertRepository.findByUserAndStatusOrderByCreatedAtDesc(user, status);
		
		return alerts.stream().map(this::toResponse).toList();
	}
	
	@Transactional
	public void markRead(Long userId, Long alertId) {
		Alert alert = alertRepository.findByIdAndUserId(alertId, userId)
		                             .orElseThrow(() -> new NoSuchElementException("Alert not found: " + alertId + " for userId=" + userId));
		
		alert.markRead();
		alertRepository.save(alert);
	}
	
	@Transactional
	public void markAllRead(Long userId) {
		User user = userRepository.findById(userId)
		                          .orElseThrow(() -> new NoSuchElementException("User not found: " + userId));
		
		List<Alert> activeAlerts = alertRepository.findByUserAndStatusOrderByCreatedAtDesc(user, AlertStatus.ACTIVE);
		for (Alert a : activeAlerts) {
			a.markRead();
		}
		alertRepository.saveAll(activeAlerts);
	}
	
	private AlertResponse toResponse(Alert a) {
		return new AlertResponse(
				a.getId(),
				a.getUser().getId(),
				a.getBudget().getId(),
				a.getPeriodType(),
				a.getYear(),
				a.getMonth(),
				a.getLimitAmount(),
				a.getTotalExpense(),
				a.getType(),
				a.getMessage(),
				a.getStatus(),
				a.getReadAt()
		);
	}
}