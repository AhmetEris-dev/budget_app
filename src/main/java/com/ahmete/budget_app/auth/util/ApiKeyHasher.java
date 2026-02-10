package com.ahmete.budget_app.auth.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public final class ApiKeyHasher {
	private ApiKeyHasher() {}
	
	public static String sha256Hex(String raw) {
		if (raw == null || raw.isBlank()) return null;
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			byte[] out = md.digest(raw.getBytes(StandardCharsets.UTF_8));
			StringBuilder sb = new StringBuilder(out.length * 2);
			for (byte b : out) sb.append(String.format("%02x", b));
			return sb.toString();
		} catch (Exception e) {
			throw new IllegalStateException("SHA-256 not available", e);
		}
	}
}