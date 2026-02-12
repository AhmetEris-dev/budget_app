package com.ahmete.budget_app.common.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public final class SecurityUtils {
	
	private SecurityUtils() {}
	
	public static Long currentUserId() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth == null || !(auth.getPrincipal() instanceof ApiKeyPrincipal p)) {
			throw new IllegalStateException("Unauthenticated: userId not found in SecurityContext");
		}
		return p.userId();
	}
}