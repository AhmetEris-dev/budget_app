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
		this.key = Keys.hmacShaKeyFor(props.secret().getBytes(StandardCharsets.UTF_8));
	}
	
	public String generateAccessToken(Long userId) {
		Instant now = Instant.now();
		Instant exp = now.plus(props.accessTokenMinutes(), ChronoUnit.MINUTES);
		
		return Jwts.builder()
		           .issuer(props.issuer())
		           .subject(String.valueOf(userId))
		           .issuedAt(Date.from(now))
		           .expiration(Date.from(exp))
		           .claim("typ", "access")
		           .signWith(key)
		           .compact();
	}
	
	public Long parseUserId(String token) {
		Claims claims = Jwts.parser()
		                    .verifyWith(key)
		                    .requireIssuer(props.issuer())
		                    .build()
		                    .parseSignedClaims(token)
		                    .getPayload();
		
		return Long.parseLong(claims.getSubject());
	}
	
	public boolean isValid(String token) {
		try {
			Jwts.parser().verifyWith(key).requireIssuer(props.issuer()).build().parseSignedClaims(token);
			return true;
		} catch (JwtException | IllegalArgumentException e) {
			return false;
		}
	}
}