package com.ahmete.budget_app.auth.service;

import com.ahmete.budget_app.auth.config.JwtProperties;
import com.ahmete.budget_app.auth.dto.*;
import com.ahmete.budget_app.auth.entity.RefreshToken;
import com.ahmete.budget_app.auth.repository.RefreshTokenRepository;
import com.ahmete.budget_app.common.exception.BadRequestException;
import com.ahmete.budget_app.common.exception.UnauthorizedException;
import com.ahmete.budget_app.common.security.ApiKeyHasher;
import com.ahmete.budget_app.user.entity.User;
import com.ahmete.budget_app.user.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;

@Service
public class AuthService {
	
	private final UserRepository userRepository;
	private final RefreshTokenRepository refreshTokenRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtService jwtService;
	private final JwtProperties props;
	private final ApiKeyHasher hasher;
	
	public AuthService(
			UserRepository userRepository,
			RefreshTokenRepository refreshTokenRepository,
			PasswordEncoder passwordEncoder,
			JwtService jwtService,
			JwtProperties props,
			ApiKeyHasher hasher
	) {
		this.userRepository = userRepository;
		this.refreshTokenRepository = refreshTokenRepository;
		this.passwordEncoder = passwordEncoder;
		this.jwtService = jwtService;
		this.props = props;
		this.hasher = hasher;
	}
	
	@Transactional
	public TokenResponse register(RegisterRequest req) {
		if (userRepository.existsByEmail(req.email())) {
			throw new BadRequestException("Email already exists: " + req.email());
		}
		
		User u = new User();
		u.setEmail(req.email());
		u.setFullName(req.fullName());
		u.setPasswordHash(passwordEncoder.encode(req.password()));
		
		User saved = userRepository.save(u);
		
		String access = jwtService.generateAccessToken(saved.getId());
		String refreshRaw = generateRefreshRaw();
		saveRefresh(saved, refreshRaw);
		
		return new TokenResponse(saved.getId(), access, refreshRaw);
	}
	
	@Transactional
	public TokenResponse login(LoginRequest req) {
		User user = userRepository.findByEmail(req.email())
		                          .orElseThrow(() -> new UnauthorizedException("Invalid credentials"));
		
		if (user.getPasswordHash() == null || !passwordEncoder.matches(req.password(), user.getPasswordHash())) {
			throw new UnauthorizedException("Invalid credentials");
		}
		
		String access = jwtService.generateAccessToken(user.getId());
		String refreshRaw = generateRefreshRaw();
		saveRefresh(user, refreshRaw);
		
		return new TokenResponse(user.getId(), access, refreshRaw);
	}
	
	@Transactional
	public TokenResponse refresh(RefreshRequest req) {
		String raw = req.refreshToken();
		String hash = hasher.sha256Hex(raw);
		
		RefreshToken rt = refreshTokenRepository.findByTokenHash(hash)
		                                        .orElseThrow(() -> new UnauthorizedException("Invalid refresh token"));
		
		if (rt.isRevoked() || rt.isExpired()) {
			throw new UnauthorizedException("Refresh token expired/revoked");
		}
		
		// rotate: eskiyi revoke, yenisini üret
		rt.setRevokedAt(LocalDateTime.now());
		
		String newAccess = jwtService.generateAccessToken(rt.getUser().getId());
		String newRefreshRaw = generateRefreshRaw();
		saveRefresh(rt.getUser(), newRefreshRaw);
		
		return new TokenResponse(rt.getUser().getId(), newAccess, newRefreshRaw);
	}
	
	private void saveRefresh(User user, String refreshRaw) {
		String hash = hasher.sha256Hex(refreshRaw);
		
		RefreshToken rt = new RefreshToken();
		rt.setUser(user);
		rt.setTokenHash(hash);
		rt.setExpiresAt(LocalDateTime.now().plusDays(props.refreshTokenDays()));
		rt.setCreatedAt(LocalDateTime.now());
		refreshTokenRepository.save(rt);
	}
	
	private String generateRefreshRaw() {
		byte[] bytes = new byte[48];
		new SecureRandom().nextBytes(bytes);
		return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
	}
}