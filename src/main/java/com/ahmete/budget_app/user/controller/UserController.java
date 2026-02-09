package com.ahmete.budget_app.user.controller;

import com.ahmete.budget_app.constants.RestApis;
import com.ahmete.budget_app.user.dto.request.CreateUserRequest;
import com.ahmete.budget_app.user.dto.response.UserResponse;
import com.ahmete.budget_app.user.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(RestApis.User.ROOT)
public class UserController {
	
	private final UserService userService;
	public UserController(UserService userService) { this.userService = userService; }
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public UserResponse create(@Valid @RequestBody CreateUserRequest request) {
		return userService.create(request);
	}
	
	@GetMapping(RestApis.User.BY_ID)
	public UserResponse getById(@PathVariable Long id) {
		return userService.getById(id);
	}
}