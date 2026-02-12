package com.ahmete.budget_app.auth.controller;

import com.ahmete.budget_app.auth.dto.*;
import com.ahmete.budget_app.auth.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
	
	private final AuthService authService;
	
	public AuthController(AuthService authService) {
		this.authService = authService;
	}
	
	@PostMapping("/register")
	@ResponseStatus(HttpStatus.CREATED)
	public TokenResponse register(@Valid @RequestBody RegisterRequest req) {
		return authService.register(req);
	}
	
	@PostMapping("/login")
	public TokenResponse login(@Valid @RequestBody LoginRequest req) {
		return authService.login(req);
	}
	
	@PostMapping("/refresh")
	public TokenResponse refresh(@Valid @RequestBody RefreshRequest req) {
		return authService.refresh(req);
	}
}