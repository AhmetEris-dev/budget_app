package com.ahmete.budget_app.auth.entity;

import com.ahmete.budget_app.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "refresh_tokens")
@Getter @Setter
public class RefreshToken {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name="user_id", nullable=false)
	private User user;
	
	@Column(name="token_hash", nullable=false, unique=true, length=64)
	private String tokenHash;
	
	@Column(name="expires_at", nullable=false)
	private LocalDateTime expiresAt;
	
	@Column(name="revoked_at")
	private LocalDateTime revokedAt;
	
	@Column(name="created_at", nullable=false)
	private LocalDateTime createdAt = LocalDateTime.now();
	
	public boolean isExpired() {
		return expiresAt.isBefore(LocalDateTime.now());
	}
	
	public boolean isRevoked() {
		return revokedAt != null;
	}
}