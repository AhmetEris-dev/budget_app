package com.ahmete.budget_app.common.security;

import com.ahmete.budget_app.apikey.entity.ApiKey;
import com.ahmete.budget_app.apikey.repository.ApiKeyRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public class ApiKeyAuthFilter extends OncePerRequestFilter {
	
	public static final String HEADER = "X-API-KEY";
	
	private final ApiKeyRepository apiKeyRepository;
	private final ApiKeyHasher hasher;
	private final ObjectMapper objectMapper;
	
	public ApiKeyAuthFilter(ApiKeyRepository apiKeyRepository, ApiKeyHasher hasher, ObjectMapper objectMapper) {
		this.apiKeyRepository = apiKeyRepository;
		this.hasher = hasher;
		this.objectMapper = objectMapper;
	}
	
	@Override
	protected boolean shouldNotFilter(HttpServletRequest req) {
		String path = req.getServletPath();
		
		// public alanlarda filter çalışmasın (swagger + user onboarding)
		return path.startsWith("/swagger-ui")
				|| path.startsWith("/v3/api-docs")
				|| "/swagger-ui.html".equals(path)
				|| path.startsWith("/api/v1/users");
	}
	
	@Override
	protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
			throws ServletException, IOException {
		
		// CORS preflight
		if ("OPTIONS".equalsIgnoreCase(req.getMethod())) {
			chain.doFilter(req, res);
			return;
		}
		
		// zaten auth olmuşsa elleme
		if (SecurityContextHolder.getContext().getAuthentication() != null) {
			chain.doFilter(req, res);
			return;
		}
		
		// header yoksa geç -> protected endpointlerde SecurityConfig 401 döner
		String raw = req.getHeader(HEADER);
		if (raw == null || raw.isBlank()) {
			chain.doFilter(req, res);
			return;
		}
		
		String hash = hasher.sha256Hex(raw);
		
		ApiKey apiKey = apiKeyRepository.findByKeyHashAndActiveTrue(hash).orElse(null);
		if (apiKey == null) {
			res.setStatus(401);
			res.setContentType(MediaType.APPLICATION_JSON_VALUE);
			
			Map<String, Object> body = new LinkedHashMap<>();
			body.put("status", 401);
			body.put("error", "Unauthorized");
			body.put("message", "Invalid X-API-KEY header");
			body.put("path", req.getRequestURI());
			
			objectMapper.writeValue(res.getOutputStream(), body);
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