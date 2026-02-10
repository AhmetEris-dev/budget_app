package com.ahmete.budget_app.auth.service;

import com.ahmete.budget_app.auth.entity.ApiKey;
import com.ahmete.budget_app.auth.repository.ApiKeyRepository;
import com.ahmete.budget_app.auth.util.ApiKeyHasher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;

@Service
public class ApiKeyService {
	
	private final ApiKeyRepository repo;
	private final SecureRandom random = new SecureRandom();
	
	public ApiKeyService(ApiKeyRepository repo) {
		this.repo = repo;
	}
	
	@Transactional
	public GeneratedKey create(String clientName) {
		String raw = generateRawKey();
		String hash = ApiKeyHasher.sha256Hex(raw);
		
		ApiKey saved = repo.save(new ApiKey(clientName, hash));
		return new GeneratedKey(saved.getId(), clientName, raw);
	}
	
	private String generateRawKey() {
		byte[] bytes = new byte[32];
		random.nextBytes(bytes);
		StringBuilder sb = new StringBuilder(bytes.length * 2);
		for (byte b : bytes) sb.append(String.format("%02x", b));
		return sb.toString();
	}
	
	public record GeneratedKey(Long id, String clientName, String apiKey) {}
}