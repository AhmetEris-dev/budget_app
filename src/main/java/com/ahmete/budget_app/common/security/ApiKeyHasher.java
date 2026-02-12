package com.ahmete.budget_app.common.security;

import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

@Component
public class ApiKeyHasher {
	
	public String sha256Hex(String raw) {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			byte[] digest = md.digest(raw.getBytes(StandardCharsets.UTF_8));
			return toHex(digest);
		} catch (Exception e) {
			throw new IllegalStateException("SHA-256 not available", e);
		}
	}
	
	private static String toHex(byte[] bytes) {
		StringBuilder sb = new StringBuilder(bytes.length * 2);
		for (byte b : bytes) sb.append(String.format("%02x", b));
		return sb.toString();
	}
}