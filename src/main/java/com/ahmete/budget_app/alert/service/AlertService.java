package com.ahmete.budget_app.alert.service;

import com.ahmete.budget_app.alert.dto.response.AlertResponse;
import com.ahmete.budget_app.alert.entity.Alert;
import com.ahmete.budget_app.alert.repository.AlertRepository;
import com.ahmete.budget_app.user.entity.User;
import com.ahmete.budget_app.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class AlertService {
	
	private final AlertRepository alertRepository;
	private final UserRepository userRepository;
	
	public AlertService(AlertRepository alertRepository,
	                    UserRepository userRepository) {
		this.alertRepository = alertRepository;
		this.userRepository = userRepository;
	}
	
	public List<AlertResponse> listByUser(Long userId) {
		User user = userRepository.findById(userId)
		                          .orElseThrow(() -> new NoSuchElementException("User not found: " + userId));
		
		return alertRepository.findByUserOrderByCreatedAtDesc(user)
		                      .stream()
		                      .map(this::toResponse)
		                      .toList();
	}
	
	private AlertResponse toResponse(Alert a) {
		return new AlertResponse(a.getId(), a.getUser().getId(), a.getBudget()
		                                                          .getId(), a.getPeriodType(), a.getYear(), a.getMonth(), a.getLimitAmount(), a.getTotalExpense(), a.getType(), a.getMessage());
		
	}
}