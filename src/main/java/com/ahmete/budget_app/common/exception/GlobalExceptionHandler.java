package com.ahmete.budget_app.common.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@RestControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ApiError> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest req) {
		Map<String, String> errors = new HashMap<>();
		for (FieldError fe : ex.getBindingResult().getFieldErrors()) {
			errors.put(fe.getField(), fe.getDefaultMessage());
		}
		ApiError body = new ApiError(
				LocalDateTime.now(),
				HttpStatus.BAD_REQUEST.value(),
				"Bad Request",
				"Validation failed",
				req.getRequestURI(),
				errors
		);
		return ResponseEntity.badRequest().body(body);
	}
	
	@ExceptionHandler(NoSuchElementException.class)
	public ResponseEntity<ApiError> handleNotFound(NoSuchElementException ex, HttpServletRequest req) {
		ApiError body = new ApiError(
				LocalDateTime.now(),
				HttpStatus.NOT_FOUND.value(),
				"Not Found",
				ex.getMessage(),
				req.getRequestURI(),
				null
		);
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
	}
	
	@ExceptionHandler(IllegalStateException.class)
	public ResponseEntity<ApiError> handleBadState(IllegalStateException ex, HttpServletRequest req) {
		ApiError body = new ApiError(
				LocalDateTime.now(),
				HttpStatus.BAD_REQUEST.value(),
				"Bad Request",
				ex.getMessage(),
				req.getRequestURI(),
				null
		);
		return ResponseEntity.badRequest().body(body);
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ApiError> handleGeneric(Exception ex, HttpServletRequest req) {
		ApiError body = new ApiError(
				LocalDateTime.now(),
				HttpStatus.INTERNAL_SERVER_ERROR.value(),
				"Internal Server Error",
				ex.getMessage(),
				req.getRequestURI(),
				null
		);
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
	}
	@ExceptionHandler(BadRequestException.class)
	public ResponseEntity<ApiError> handleBadRequest(BadRequestException ex, HttpServletRequest req) {
		ApiError body = new ApiError(
				LocalDateTime.now(),
				HttpStatus.BAD_REQUEST.value(),
				"Bad Request",
				ex.getMessage(),
				req.getRequestURI(),
				null
		);
		return ResponseEntity.badRequest().body(body);
	}
	
	@ExceptionHandler(NotFoundException.class)
	public ResponseEntity<ApiError> handleNotFoundCustom(NotFoundException ex, HttpServletRequest req) {
		ApiError body = new ApiError(
				LocalDateTime.now(),
				HttpStatus.NOT_FOUND.value(),
				"Not Found",
				ex.getMessage(),
				req.getRequestURI(),
				null
		);
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
	}
	
	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<ApiError> handleIllegalArg(IllegalArgumentException ex, HttpServletRequest req) {
		ApiError body = new ApiError(
				LocalDateTime.now(),
				HttpStatus.BAD_REQUEST.value(),
				"Bad Request",
				ex.getMessage(),
				req.getRequestURI(),
				null
		);
		return ResponseEntity.badRequest().body(body);
	}
	
	@ExceptionHandler(UnauthorizedException.class)
	public ResponseEntity<ApiError> handleUnauthorized(UnauthorizedException ex, HttpServletRequest req) {
		ApiError body = new ApiError(
				LocalDateTime.now(),
				HttpStatus.UNAUTHORIZED.value(),
				"Unauthorized",
				ex.getMessage(),
				req.getRequestURI(),
				null
		);
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(body);
	}
	
}