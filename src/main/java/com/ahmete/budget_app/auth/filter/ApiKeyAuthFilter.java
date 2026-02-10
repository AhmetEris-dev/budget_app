package com.ahmete.budget_app.auth.filter;

import com.ahmete.budget_app.auth.repository.ApiKeyRepository;
import com.ahmete.budget_app.auth.util.ApiKeyHasher;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

public class ApiKeyAuthFilter extends OncePerRequestFilter {
	
	public static final String HEADER = "X-API-KEY";
	private final ApiKeyRepository apiKeyRepository;
	
	public ApiKeyAuthFilter(ApiKeyRepository apiKeyRepository) {
		this.apiKeyRepository = apiKeyRepository;
	}
	
	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) {
		if ("OPTIONS".equalsIgnoreCase(request.getMethod())) return true;
		
		String path = request.getRequestURI();
		return path.startsWith("/api/v1/api-keys")   // ✅ BOOTSTRAP: key üretme
				|| path.startsWith("/v3/api-docs")
				|| path.startsWith("/swagger-ui")
				|| path.startsWith("/actuator/health");
	}
	
	@Override
	protected void doFilterInternal(
			HttpServletRequest req,
			HttpServletResponse res,
			FilterChain chain
	) throws ServletException, IOException {
		
		String rawKey = req.getHeader(HEADER);
		
		if (rawKey == null || rawKey.isBlank()) {
			unauthorized(res, "Missing X-API-KEY header");
			return;
		}
		
		String hash = ApiKeyHasher.sha256Hex(rawKey);
		boolean ok = apiKeyRepository.findByKeyHashAndActiveTrue(hash).isPresent();
		
		if (!ok) {
			unauthorized(res, "Invalid API key");
			return;
		}
		
		var auth = new UsernamePasswordAuthenticationToken(
				"api-client",
				null,
				List.of(new SimpleGrantedAuthority("ROLE_API"))
		);
		SecurityContextHolder.getContext().setAuthentication(auth);
		
		chain.doFilter(req, res);
	}
	
	private void unauthorized(HttpServletResponse res, String msg) throws IOException {
		res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		res.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
		res.getWriter().write("{\"status\":401,\"error\":\"Unauthorized\",\"message\":\"" + msg + "\"}");
	}
}