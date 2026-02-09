package com.ahmete.budget_app.common.exception;

public class BadRequestException extends RuntimeException {
	public BadRequestException(String message) { super(message); }
}