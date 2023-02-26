package com.example.demo.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dao.TokensDao;
import com.example.demo.dto.TokenDto;
import com.example.demo.entity.Tokens;
import com.example.demo.entity.Users;
import com.example.demo.exception.RefreshTokenNotFoundException;
import com.example.demo.service.TokenService;
import com.example.demo.util.JwtUtil;

@Service
public class TokenServiceImpl implements TokenService {

	@Autowired
	private TokensDao tokensDao;

	/**
	 * Generate the new refresh token from old refresh token and insert into DB
	 * 
	 * @param refreshToken The old refresh token
	 * @return refreshToken The new refresh token
	 */
	@Override
	public TokenDto refreshToken(String refreshToken) {
		// Get refresh token in DB
		Tokens tokens = tokensDao.findByRefreshToken(refreshToken);
		// If refresh token isn't exist in DB
		if (tokens == null) {
			throw new RefreshTokenNotFoundException("Refresh Token isn't existing");
		}

		// If refresh token isn't exist in DB
		// Create claims for JWT body
		Users user = tokens.getUser();
		Map<String, Object> claims = new HashMap<>();
		claims.put("email", user.getEmail());
		claims.put("userId", user.getId());
		claims.put("lastName", user.getLastName());
		claims.put("firstName", user.getFirstName());
		// Generate token expires in 1 hour
		String token = JwtUtil.createToken(claims, 1);
		// Generate token expires in 30 days
		String newRefreshToken = JwtUtil.createToken(claims, 30 * 24);
		// Create tokenDto to return
		TokenDto tokenDto = new TokenDto();
		tokenDto.setToken(token);
		tokenDto.setRefreshToken(newRefreshToken);
		return tokenDto;
	}

	/**
	 * Delete all token belong to certain user account
	 * 
	 * @param userId
	 */
	@Transactional
	@Override
	public void deleteByUserId(Integer userId) {
		// delete all refresh token belong to user account
		tokensDao.deleteByUserId(userId);
	}

}
