package com.ahmete.budget_app.apikey.service;

import com.ahmete.budget_app.apikey.dto.request.CreateApiKeyRequest;
import com.ahmete.budget_app.apikey.dto.response.CreateApiKeyResponse;
import com.ahmete.budget_app.apikey.entity.ApiKey;
import com.ahmete.budget_app.apikey.repository.ApiKeyRepository;
import com.ahmete.budget_app.common.security.ApiKeyHasher;
import com.ahmete.budget_app.user.entity.User;
import com.ahmete.budget_app.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class ApiKeyService {
	
	private final ApiKeyRepository apiKeyRepository;
	private final UserRepository userRepository;
	private final ApiKeyHasher hasher;
	
	@Transactional
	public CreateApiKeyResponse create(Long userId, CreateApiKeyRequest req) {
		User user = userRepository.findById(userId)
		                          .orElseThrow(() -> new NoSuchElementException("User not found: " + userId));
		
		String rawKey = generateRawKey();
		String hash = hasher.sha256Hex(rawKey);
		
		ApiKey apiKey = new ApiKey();
		apiKey.setUser(user);
		apiKey.setClientName(req.clientName());
		apiKey.setKeyHash(hash);
		apiKey.setActive(true);
		apiKey.setCreatedAt(LocalDateTime.now());
		
		ApiKey saved = apiKeyRepository.save(apiKey);
		
		return new CreateApiKeyResponse(
				saved.getId(),
				user.getId(),
				saved.getClientName(),
				rawKey,
				saved.getCreatedAt()
		);
	}
	
	
	private String generateRawKey() {
		byte[] bytes = new byte[32];
		new SecureRandom().nextBytes(bytes);
		return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
	}
	@Transactional
	public String createInitialKeyForUser(User user, String clientName) {
		String rawKey = generateRawKey();
		String hash = hasher.sha256Hex(rawKey);
		
		ApiKey apiKey = new ApiKey();
		apiKey.setUser(user);
		apiKey.setClientName(clientName);
		apiKey.setKeyHash(hash);
		apiKey.setActive(true);
		apiKey.setCreatedAt(LocalDateTime.now());
		
		apiKeyRepository.save(apiKey);
		return rawKey; // sadece buradan dönsün
	}
	
	// generateRawKey() zaten sende var, olduğu gibi kalsın
}