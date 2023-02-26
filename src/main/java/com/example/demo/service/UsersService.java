package com.example.demo.service;

import com.example.demo.dto.UserTokenDto;
import com.example.demo.dto.UserDto;
import com.example.demo.entity.Users;

public interface UsersService {

	/**
	 * Save new user into database
	 * 
	 * @param user Users Entity
	 * @return userDto Return new user information that is registered
	 */
	public UserDto save(Users user);

	/**
	 * Sign in
	 * 
	 * @param user Users Entity
	 * @return userTokenDto Return user information, access token and refresh token
	 */
	public UserTokenDto signIn(Users user);
}
