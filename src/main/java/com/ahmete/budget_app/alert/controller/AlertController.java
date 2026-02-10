package com.ahmete.budget_app.alert.controller;

import com.ahmete.budget_app.alert.dto.response.AlertResponse;
import com.ahmete.budget_app.alert.service.AlertService;
import com.ahmete.budget_app.constants.RestApis;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(RestApis.Alert.ROOT)
public class AlertController {
	
	private final AlertService alertService;
	
	public AlertController(AlertService alertService) {
		this.alertService = alertService;
	}
	
	@GetMapping
	public List<AlertResponse> listByUser(@RequestParam Long userId) {
		return alertService.listByUser(userId);
	}
}