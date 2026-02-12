package com.ahmete.budget_app.common.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.List;

/**
 * Bizim sistemde şimdilik role/authority yok.
 * İstersen ileride burada authority ekleyebilirsin.
 */
public class ApiKeyAuthenticationToken extends AbstractAuthenticationToken {
	
	private final ApiKeyPrincipal principal;
	
	public ApiKeyAuthenticationToken(ApiKeyPrincipal principal) {
		super(List.of());
		this.principal = principal;
		setAuthenticated(true);
	}
	
	public ApiKeyAuthenticationToken(ApiKeyPrincipal principal, Collection<? extends GrantedAuthority> authorities) {
		super(authorities);
		this.principal = principal;
		setAuthenticated(true);
	}
	
	@Override
	public Object getCredentials() {
		return ""; // credential tutmuyoruz
	}
	
	@Override
	public ApiKeyPrincipal getPrincipal() {
		return principal;
	}
}