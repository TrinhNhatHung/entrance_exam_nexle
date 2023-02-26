package com.example.demo.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Tokens;

@Repository
public interface TokensDao extends JpaRepository<Tokens, Integer> {
	/**
	 * Get token entity by refresh token
	 * 
	 * @param refreshToken
	 * @return token entity
	 */
	Tokens findByRefreshToken(String refreshToken);

	/**
	 * Delete token entity by user_id
	 * 
	 * @param userId
	 */
	void deleteByUserId(Integer userId);
}
