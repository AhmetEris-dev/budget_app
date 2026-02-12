package com.ahmete.budget_app.user.controller;

import com.ahmete.budget_app.common.security.SecurityUtils;
import com.ahmete.budget_app.constants.RestApis;
import com.ahmete.budget_app.user.dto.response.UserResponse;
import com.ahmete.budget_app.user.service.UserService;
import org.springframework.web.bind.annotation.*;

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
	
	// İstersen bunu tamamen kaldır.
	// Eğer admin gibi rol sistemi kuracaksan ileride geri eklersin.
	// @GetMapping(RestApis.User.BY_ID)
	// public UserResponse getById(@PathVariable Long id) {
	//     return userService.getById(id);
	// }
}