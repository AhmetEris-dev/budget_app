package com.ahmete.budget_app.auth.controller;

import com.ahmete.budget_app.auth.service.ApiKeyService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/api-keys")
public class ApiKeyController {
	
	private final ApiKeyService service;
	
	public ApiKeyController(ApiKeyService service) {
		this.service = service;
	}
	
	// İlk key’i üretmek için bunu geçici olarak permitAll yapacağız,
	// veya bootstrap key ile koruruz. Şimdilik hızlı gidelim:
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ApiKeyService.GeneratedKey create(@RequestParam String clientName) {
		return service.create(clientName);
	}
}