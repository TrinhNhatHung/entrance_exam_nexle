package com.example.demo.service;

import com.example.demo.dto.TokenDto;

public interface TokenService {

	/**
	 * Generate the new refresh token from old refresh token and insert into DB
	 * 
	 * @param refreshToken The old refresh token
	 * @return refreshToken The new refresh token
	 */
	public TokenDto refreshToken(String refreshToken);

	/**
	 * Delete all token belong to certain user account
	 * 
	 * @param userId
	 */
	public void deleteByUserId(Integer userId);
}
