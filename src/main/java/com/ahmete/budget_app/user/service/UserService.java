package com.ahmete.budget_app.user.service;

import com.ahmete.budget_app.common.exception.BadRequestException;
import com.ahmete.budget_app.common.exception.NotFoundException;
import com.ahmete.budget_app.user.dto.request.CreateUserRequest;
import com.ahmete.budget_app.user.dto.response.UserResponse;
import com.ahmete.budget_app.user.entity.User;
import com.ahmete.budget_app.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {
	
	private final UserRepository userRepository;
	public UserService(UserRepository userRepository) { this.userRepository = userRepository; }
	
	@Transactional
	public UserResponse create(CreateUserRequest request) {
		if (userRepository.existsByEmail(request.email())) {
			throw new BadRequestException("Email already exists: " + request.email());
		}
		
		User user = new User();
		user.setEmail(request.email());
		user.setFullName(request.fullName());
		
		User saved = userRepository.save(user);
		return toResponse(saved);
	}
	
	@Transactional(readOnly = true)
	public UserResponse getById(Long id) {
		User user = userRepository.findById(id)
		                          .orElseThrow(() -> new NotFoundException("User not found: " + id));
		return toResponse(user);
	}
	
	private UserResponse toResponse(User u) {
		return new UserResponse(u.getId(), u.getEmail(), u.getFullName());
	}
}