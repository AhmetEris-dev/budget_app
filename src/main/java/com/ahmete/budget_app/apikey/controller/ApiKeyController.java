package com.ahmete.budget_app.apikey.controller;

import com.ahmete.budget_app.apikey.dto.request.CreateApiKeyRequest;
import com.ahmete.budget_app.apikey.dto.response.CreateApiKeyResponse;
import com.ahmete.budget_app.apikey.service.ApiKeyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/api-keys")
@RequiredArgsConstructor
public class ApiKeyController {
	
	private final ApiKeyService apiKeyService;
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public CreateApiKeyResponse create(@Valid @RequestBody CreateApiKeyRequest request) {
		return apiKeyService.create(request);
	}
}