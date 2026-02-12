package com.ahmete.budget_app.common.security;

import org.springframework.security.core.context.SecurityContextHolder;

public final class SecurityUtils {
	private SecurityUtils() {}
	
	public static Long currentUserId() {
		var auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth == null || !(auth.getPrincipal() instanceof ApiKeyPrincipal p)) {
			throw new IllegalStateException("No authenticated API key");
		}
		return p.userId();
	}
}