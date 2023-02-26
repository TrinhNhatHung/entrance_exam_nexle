package com.example.demo.exception;

public class EmailExistException extends RuntimeException {

	private static final long serialVersionUID = -3264302544963616270L;

	/**
	 * Constructor
	 */
	public EmailExistException() {

	}

	/**
	 * Constructor
	 * 
	 * @param message
	 */
	public EmailExistException(String message) {
		super(message);
	}
}
