package com.ahmete.budget_app.user.service;

import com.ahmete.budget_app.apikey.service.ApiKeyService;
import com.ahmete.budget_app.common.exception.BadRequestException;
import com.ahmete.budget_app.common.exception.NotFoundException;
import com.ahmete.budget_app.user.dto.request.CreateUserRequest;
import com.ahmete.budget_app.user.dto.response.CreateUserResponse;
import com.ahmete.budget_app.user.dto.response.UserResponse;
import com.ahmete.budget_app.user.entity.User;
import com.ahmete.budget_app.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {
	
	private final UserRepository userRepository;
	private final ApiKeyService apiKeyService;
	
	public UserService(UserRepository userRepository, ApiKeyService apiKeyService) {
		this.userRepository = userRepository;
		this.apiKeyService = apiKeyService;
	}
	
	@Transactional
	public CreateUserResponse create(CreateUserRequest request) {
		if (userRepository.existsByEmail(request.email())) {
			throw new BadRequestException("Email already exists: " + request.email());
		}
		
		User user = new User();
		user.setEmail(request.email());
		user.setFullName(request.fullName());
		
		User saved = userRepository.save(user);
		
		// ✅ bootstrap: ilk key burada üretilecek
		String rawKey = apiKeyService.createInitialKeyForUser(saved, "bootstrap");
		
		return new CreateUserResponse(
				saved.getId(),
				saved.getEmail(),
				saved.getFullName(),
				rawKey,
				saved.getCreatedAt()
		);
	}
	
	@Transactional(readOnly = true)
	public UserResponse getById(Long id) {
		User user = userRepository.findById(id)
		                          .orElseThrow(() -> new NotFoundException("User not found: " + id));
		return new UserResponse(user.getId(), user.getEmail(), user.getFullName());
	}
}