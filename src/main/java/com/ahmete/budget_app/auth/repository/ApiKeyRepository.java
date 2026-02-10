package com.ahmete.budget_app.auth.repository;

import com.ahmete.budget_app.auth.entity.ApiKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ApiKeyRepository extends JpaRepository<ApiKey, Long> {
	Optional<ApiKey> findByKeyHashAndActiveTrue(String keyHash);
	boolean existsByKeyHash(String keyHash);
}