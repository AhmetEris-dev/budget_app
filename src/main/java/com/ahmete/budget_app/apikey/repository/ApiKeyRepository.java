package com.ahmete.budget_app.apikey.repository;

import com.ahmete.budget_app.apikey.entity.ApiKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ApiKeyRepository extends JpaRepository<ApiKey, Long> {
	
	Optional<ApiKey> findByKeyHashAndActiveTrue(String keyHash);
}