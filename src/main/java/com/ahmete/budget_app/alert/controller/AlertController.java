package com.ahmete.budget_app.alert.controller;

import com.ahmete.budget_app.alert.entity.AlertStatus;
import com.ahmete.budget_app.alert.service.AlertService;
import com.ahmete.budget_app.common.dto.response.PageResponse;
import com.ahmete.budget_app.alert.dto.response.AlertResponse;
import com.ahmete.budget_app.common.security.SecurityUtils;
import com.ahmete.budget_app.constants.RestApis;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(RestApis.Alert.ROOT)
public class AlertController {
	
	private final AlertService alertService;
	
	public AlertController(AlertService alertService) {
		this.alertService = alertService;
	}
	
	@GetMapping
	public PageResponse<AlertResponse> list(
			@RequestParam(required = false) AlertStatus status,
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size
	) {
		Long userId = SecurityUtils.currentUserId();
		return alertService.listByUser(userId, status, page, size);
	}
	
	@PatchMapping("/{id}/read")
	public ResponseEntity<Void> markRead(@PathVariable Long id) {
		Long userId = SecurityUtils.currentUserId();
		alertService.markRead(userId, id);
		return ResponseEntity.noContent().build();
	}
}