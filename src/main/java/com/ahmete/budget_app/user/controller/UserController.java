package com.ahmete.budget_app.user.controller;

import com.ahmete.budget_app.common.security.SecurityUtils;
import com.ahmete.budget_app.constants.RestApis;
import com.ahmete.budget_app.user.dto.response.UserResponse;
import com.ahmete.budget_app.user.service.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.web.bind.annotation.*;


@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping(RestApis.User.ROOT)
public class UserController {
	
	private final UserService userService;
	public UserController(UserService userService) { this.userService = userService; }
	
	// JWT ile kendi bilgim
	@GetMapping("/me")
	public UserResponse me() {
		Long userId = SecurityUtils.currentUserId();
		return userService.getById(userId);
	}
	

}