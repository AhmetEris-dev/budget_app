package com.ahmete.budget_app.user.service;

import com.ahmete.budget_app.user.entity.User;
import com.ahmete.budget_app.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {
	
	private final UserRepository userRepository;
	
	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	
	@Transactional
	public User createUser(String email, String fullName) {
		User user = new User();
		user.setEmail(email);
		user.setFullName(fullName);
		return userRepository.save(user);
	}
}