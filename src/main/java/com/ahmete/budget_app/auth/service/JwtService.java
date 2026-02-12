package com.ahmete.budget_app.auth.service;

import com.ahmete.budget_app.auth.config.JwtProperties;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Service
public class JwtService {
	
	private final JwtProperties props;
	private final SecretKey key;
	
	public JwtService(JwtProperties props) {
		this.props = props;
		
		if (props.secret() == null || props.secret().isBlank()) {
			throw new IllegalStateException("JWT secret is missing. Set spring.security.jwt.secret in yml/env");
		}
		
		// HS256 için secret en az 32 byte olsun (pratik)
		byte[] bytes = props.secret().getBytes(StandardCharsets.UTF_8);
		if (bytes.length < 32) {
			throw new IllegalStateException("JWT secret too short. Use at least 32 chars.");
		}
		
		this.key = Keys.hmacShaKeyFor(bytes);
	}
	
	public String generateAccessToken(Long userId) {
		Instant now = Instant.now();
		Instant exp = now.plus(props.accessTokenMinutes() == null ? 15 : props.accessTokenMinutes(), ChronoUnit.MINUTES);
		
		return Jwts.builder()
		           .issuer(props.issuer())
		           .subject(String.valueOf(userId))
		           .issuedAt(Date.from(now))
		           .expiration(Date.from(exp))
		           .signWith(key, Jwts.SIG.HS256)
		           .compact();
	}
	
	public boolean isValid(String token) {
		try {
			parseAllClaims(token);
			return true;
		} catch (JwtException | IllegalArgumentException e) {
			return false;
		}
	}
	
	public Long parseUserId(String token) {
		Claims c = parseAllClaims(token);
		return Long.parseLong(c.getSubject());
	}
	
	private Claims parseAllClaims(String token) {
		return Jwts.parser()
		           .verifyWith(key)
		           .requireIssuer(props.issuer())
		           .build()
		           .parseSignedClaims(token)
		           .getPayload();
	}
}