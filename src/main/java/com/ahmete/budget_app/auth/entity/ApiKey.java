package com.ahmete.budget_app.auth.entity;

import com.ahmete.budget_app.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Table(name = "api_keys", indexes = {
		@Index(name = "idx_api_keys_key_hash", columnList = "key_hash", unique = true)
})
@Getter
public class ApiKey extends BaseEntity {
	
	@Column(name = "client_name", nullable = false, length = 100)
	private String clientName;
	
	@Column(name = "key_hash", nullable = false, length = 64, unique = true)
	private String keyHash;
	
	@Column(name = "active", nullable = false)
	private boolean active = true;
	
	@Column(name = "revoked_at")
	private LocalDateTime revokedAt;
	
	protected ApiKey() {}
	
	public ApiKey(String clientName, String keyHash) {
		this.clientName = clientName;
		this.keyHash = keyHash;
		this.active = true;
	}
	
	public void revoke() {
		this.active = false;
		this.revokedAt = LocalDateTime.now();
	}
}