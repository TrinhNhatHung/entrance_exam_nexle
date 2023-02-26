package com.example.demo.util;

public class ValidationUtil {

	/**
	 * Validate email format
	 * 
	 * @param email
	 * @return In correct email format or not
	 */
	public static boolean validateEmail(String email) {
		if (email.matches("^[0-9A-Za-z]+(\\.[0-9A-Za-z]+)*@[0-9A-Za-z]+(\\.[0-9A-Za-z]+)*$")) {
			return true;
		}
		return false;
	}

	/**
	 * Validate password Check password must be between 8-20 characters
	 * 
	 * @param password
	 * @return valid or invalid password
	 */
	public static boolean validatePassword(String password) {

		// Check password must be between 8-20 characters
		if (password.length() >= 8 && password.length() <= 20) {
			return true;
		}
		return false;
	}
}
