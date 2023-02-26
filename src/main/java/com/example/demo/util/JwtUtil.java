package com.example.demo.util;

import java.util.Date;
import java.util.Map;
import java.util.function.Function;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class JwtUtil {

	/**
	 * Secret key for JWT
	 */
	private static String secret = "nexle";

	/**
	 * Create JWT
	 * 
	 * @param claims   the JWT claims to be set as the JWT body
	 * @param duration Expire time by hour
	 * @return JWT
	 */
	public static String createToken(Map<String, Object> claims, int duration) {
		// Calculation expire time by millisecond
		long expireTime = 1000 * 60 * 60 * duration;
		// Generate JWT
		return Jwts.builder().setClaims(claims).setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + expireTime))
				.signWith(SignatureAlgorithm.HS256, secret).compact();
	}

	/**
	 * Get all claims in JWT body
	 * 
	 * @param token
	 * @return the JWT body
	 */
	public static Claims extractAllClaims(String token) {
		return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
	}

	/**
	 * Get a claim in JWT body
	 * 
	 * @param <T>            the type of the input to the function
	 * @param token          JWT
	 * @param claimsResolver Functional Interface to get one claim in JWT body
	 * @return A claim in JWT body
	 */
	public static <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}
}
