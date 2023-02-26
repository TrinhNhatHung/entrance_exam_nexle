package com.example.demo.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Users;

@Repository
public interface UsersDao extends JpaRepository<Users, Integer> {
	/**
	 * Get user by email
	 * 
	 * @param email
	 * @return user entity
	 */
	Users findByEmail(String email);
}
