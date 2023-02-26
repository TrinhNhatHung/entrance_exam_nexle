package com.example.demo.exception;

public class RefreshTokenNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 5832258889467087797L;

	/**
	 * Constructor
	 */
	public RefreshTokenNotFoundException() {

	}

	/**
	 * Constructor
	 * 
	 * @param message
	 */
	public RefreshTokenNotFoundException(String message) {
		super(message);
	}
}
