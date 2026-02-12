package com.ahmete.budget_app.common.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;

import java.util.List;

public class ApiKeyAuthenticationToken extends AbstractAuthenticationToken {
	
	private final ApiKeyPrincipal principal;
	
	public ApiKeyAuthenticationToken(ApiKeyPrincipal principal) {
		super(List.of()); // Şimdilik role istemiyoruz; sadece authenticated yeter
		this.principal = principal;
		setAuthenticated(true);
	}
	
	@Override
	public Object getCredentials() {
		return null; // API key raw değeri token içinde tutulmaz
	}
	
	@Override
	public ApiKeyPrincipal getPrincipal() {
		return principal;
	}
}