package com.ahmete.budget_app.auth.security;

import com.ahmete.budget_app.auth.service.JwtService;
import com.ahmete.budget_app.common.security.ApiKeyAuthenticationToken;
import com.ahmete.budget_app.common.security.ApiKeyPrincipal;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JwtAuthFilter extends OncePerRequestFilter {
	
	private final JwtService jwtService;
	
	public JwtAuthFilter(JwtService jwtService) {
		this.jwtService = jwtService;
	}
	
	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) {
		String path = request.getRequestURI();
		return path.startsWith("/api/v1/auth/")
				|| path.startsWith("/swagger-ui")
				|| path.startsWith("/v3/api-docs")
				|| path.equals("/swagger-ui.html");
	}
	
	@Override
	protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
			throws ServletException, IOException {
		
		if ("OPTIONS".equalsIgnoreCase(req.getMethod())) {
			chain.doFilter(req, res);
			return;
		}
		
		// Zaten auth set edilmişse dokunma
		if (SecurityContextHolder.getContext().getAuthentication() != null) {
			chain.doFilter(req, res);
			return;
		}
		
		String auth = req.getHeader("Authorization");
		if (auth == null || !auth.startsWith("Bearer ")) {
			chain.doFilter(req, res);
			return;
		}
		
		String token = auth.substring(7).trim();
		if (!jwtService.isValid(token)) {
			chain.doFilter(req, res);
			return;
		}
		
		Long userId = jwtService.parseUserId(token);
		
		// Var olan principal/token modelini reuse ediyorsun, sorun değil.
		ApiKeyPrincipal principal = new ApiKeyPrincipal(userId, null, "jwt");
		SecurityContextHolder.getContext().setAuthentication(new ApiKeyAuthenticationToken(principal));
		
		chain.doFilter(req, res);
	}
}