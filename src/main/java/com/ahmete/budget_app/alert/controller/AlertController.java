package com.ahmete.budget_app.alert.controller;

import com.ahmete.budget_app.alert.entity.AlertStatus;
import com.ahmete.budget_app.alert.service.AlertService;
import com.ahmete.budget_app.common.dto.response.PageResponse;
import com.ahmete.budget_app.alert.dto.response.AlertResponse;
import com.ahmete.budget_app.constants.RestApis;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(RestApis.Alert.ROOT) // /api/v1/alerts
public class AlertController {
	
	private final AlertService alertService;
	
	public AlertController(AlertService alertService) {
		this.alertService = alertService;
	}
	
	// GET /api/v1/alerts?userId=1&status=ACTIVE&page=0&size=10
	@GetMapping
	public PageResponse<AlertResponse> list(
			@RequestParam Long userId,
			@RequestParam(required = false) AlertStatus status,
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size
	) {
		return alertService.listByUser(userId, status, page, size);
	}
	
	// PATCH /api/v1/alerts/{id}/read?userId=1
	@PatchMapping("/{id}/read")
	public ResponseEntity<Void> markRead(@PathVariable Long id, @RequestParam Long userId) {
		alertService.markRead(userId, id);
		return ResponseEntity.noContent().build();
	}
}