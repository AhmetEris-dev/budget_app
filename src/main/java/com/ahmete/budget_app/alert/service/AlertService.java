package com.ahmete.budget_app.alert.service;

import com.ahmete.budget_app.alert.dto.response.AlertResponse;
import com.ahmete.budget_app.alert.entity.Alert;
import com.ahmete.budget_app.alert.entity.AlertStatus;
import com.ahmete.budget_app.alert.repository.AlertRepository;
import com.ahmete.budget_app.common.dto.response.PageResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AlertService {
	
	private final AlertRepository alertRepository;
	
	public AlertService(AlertRepository alertRepository) {
		this.alertRepository = alertRepository;
	}
	
	@Transactional(readOnly = true)
	public PageResponse<AlertResponse> listByUser(Long userId, AlertStatus status, int page, int size) {
		PageRequest pr = PageRequest.of(page, size);
		
		// default ACTIVE
		AlertStatus effectiveStatus = (status == null) ? AlertStatus.ACTIVE : status;
		
		Page<Alert> result = alertRepository.findByUserIdAndStatusOrderByCreatedAtDesc(userId, effectiveStatus, pr);
		
		return new PageResponse<>(
				result.getContent().stream().map(this::toResponse).toList(),
				result.getNumber(),
				result.getSize(),
				result.getTotalElements(),
				result.getTotalPages()
		);
	}
	
	@Transactional
	public void markRead(Long userId, Long alertId) {
		Alert alert = alertRepository.findByIdAndUserId(alertId, userId)
		                             .orElseThrow(() -> new java.util.NoSuchElementException("Alert not found: " + alertId));
		
		alert.markRead();
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