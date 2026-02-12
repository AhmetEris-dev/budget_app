package com.ahmete.budget_app.common.security;

import com.ahmete.budget_app.apikey.entity.ApiKey;
import com.ahmete.budget_app.apikey.repository.ApiKeyRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class ApiKeyAuthFilter extends OncePerRequestFilter {
	
	public static final String HEADER = "X-API-KEY";
	
	private final ApiKeyRepository apiKeyRepository;
	private final ApiKeyHasher hasher;
	
	public ApiKeyAuthFilter(ApiKeyRepository apiKeyRepository, ApiKeyHasher hasher) {
		this.apiKeyRepository = apiKeyRepository;
		this.hasher = hasher;
	}
	
	@Override
	protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
			throws ServletException, IOException {
		
		// CORS preflight
		if ("OPTIONS".equalsIgnoreCase(req.getMethod())) {
			chain.doFilter(req, res);
			return;
		}
		
		// Zaten auth olmuşsa elleme
		if (SecurityContextHolder.getContext().getAuthentication() != null) {
			chain.doFilter(req, res);
			return;
		}
		
		// Header yoksa geç (permitAll endpoint'ler zaten config'te serbest)
		String raw = req.getHeader(HEADER);
		if (raw == null || raw.isBlank()) {
			chain.doFilter(req, res);
			return;
		}
		
		// FIX: sha256Hex değil, hash
		String hash = hasher.sha256Hex(raw);
		
		ApiKey apiKey = apiKeyRepository.findByKeyHashAndActiveTrue(hash).orElse(null);
		if (apiKey == null) {
			res.setStatus(HttpStatus.UNAUTHORIZED.value());
			res.setContentType("application/json");
			res.getWriter().write("""
                {"status":401,"error":"Unauthorized","message":"Invalid X-API-KEY header"}
            """);
			return;
		}
		
		ApiKeyPrincipal principal = new ApiKeyPrincipal(
				apiKey.getUser().getId(),
				apiKey.getId(),
				apiKey.getClientName()
		);
		
		SecurityContextHolder.getContext()
		                     .setAuthentication(new ApiKeyAuthenticationToken(principal));
		
		chain.doFilter(req, res);
	}
}