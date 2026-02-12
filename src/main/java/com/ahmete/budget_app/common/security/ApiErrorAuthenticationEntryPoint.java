package com.ahmete.budget_app.common.security;

import com.ahmete.budget_app.common.exception.ApiError;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;
import java.time.LocalDateTime;

public class ApiErrorAuthenticationEntryPoint implements AuthenticationEntryPoint {
	
	private final ObjectMapper objectMapper;
	
	public ApiErrorAuthenticationEntryPoint(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}
	
	@Override
	public void commence(
			HttpServletRequest request,
			HttpServletResponse response,
			AuthenticationException authException
	) throws IOException {
		
		ApiError body = new ApiError(
				LocalDateTime.now(),
				HttpStatus.UNAUTHORIZED.value(),
				"Unauthorized",
				"Missing or invalid authentication",
				request.getRequestURI(),
				null
		);
		
		response.setStatus(HttpStatus.UNAUTHORIZED.value());
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		objectMapper.writeValue(response.getWriter(), body);
	}
}