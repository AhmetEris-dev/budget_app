package com.ahmete.budget_app.common.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

public class ApiKeyAuthenticationToken extends AbstractAuthenticationToken {
	
	private final ApiKeyPrincipal principal;
	
	public ApiKeyAuthenticationToken(ApiKeyPrincipal principal) {
		super(List.of(new SimpleGrantedAuthority("ROLE_API_KEY")));
		this.principal = principal;
		setAuthenticated(true);
	}
	
	@Override
	public Object getCredentials() {
		return ""; // API key raw değeri burada tutulmaz
	}
	
	@Override
	public ApiKeyPrincipal getPrincipal() {
		return principal;
	}
}