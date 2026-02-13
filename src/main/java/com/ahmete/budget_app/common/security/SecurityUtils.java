package com.ahmete.budget_app.common.security;

import com.ahmete.budget_app.common.exception.UnauthorizedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public final class SecurityUtils {
	
	private SecurityUtils() {}
	
	public static Long requireUserId() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth == null || !(auth.getPrincipal() instanceof ApiKeyPrincipal p) || p.userId() == null) {
			throw new UnauthorizedException("Unauthorized");
		}
		return p.userId();
	}
}