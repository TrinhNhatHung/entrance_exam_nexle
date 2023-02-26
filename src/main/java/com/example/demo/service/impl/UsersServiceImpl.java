package com.example.demo.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dao.TokensDao;
import com.example.demo.dao.UsersDao;
import com.example.demo.dto.UserDto;
import com.example.demo.dto.UserTokenDto;
import com.example.demo.entity.Tokens;
import com.example.demo.entity.Users;
import com.example.demo.exception.EmailExistException;
import com.example.demo.service.UsersService;
import com.example.demo.util.JwtUtil;

@Service
public class UsersServiceImpl implements UsersService {

	@Autowired
	private UsersDao usersDao;

	@Autowired
	private TokensDao tokenDao;

	@Autowired
	private PasswordEncoder passwordEncoder;
	
	/**
	 * Save new user into database
	 * 
	 * @param user Users Entity
	 * @return userDto Return new user information that is registered
	 */
	@Override
	public UserDto save(Users user) {
		/* Check email whether available or  not*/
		// Get user by email input
		Users existingUser = usersDao.findByEmail(user.getEmail());
		if (existingUser != null) {
			// If email is exist
			throw new EmailExistException("Email is exist");
		}
		
		// Encode password by BCryptPasswordEncoder
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		// Save new user into DB
		Users insertedUser = usersDao.save(user);
		
		// Create userDto to return the result
		UserDto userDto = new UserDto();
		userDto.setId(insertedUser.getId());
		userDto.setFirstName(insertedUser.getFirstName());
		userDto.setLastName(insertedUser.getLastName());
		userDto.setEmail(insertedUser.getEmail());
		userDto.setDisplayName(insertedUser.getFirstName() + " " + insertedUser.getLastName());
		
		return userDto;
	}

	/**
	 * Sign in
	 * 
	 * @param user Users Entity
	 * @return userTokenDto Return user information, access token and refresh token
	 */
	@Override
	@Transactional
	public UserTokenDto signIn(Users user) {
		/* Check email and password to sign in */
		// Get user by email
		Users existingUser = usersDao.findByEmail(user.getEmail());
		UserTokenDto userTokenDto = new UserTokenDto();
		// If email isn't exist or password is wrong
		if (existingUser == null || !passwordEncoder.matches(user.getPassword(), existingUser.getPassword())) {
			return userTokenDto;
		}
		
		// If email and password correct
		// Create userDto include user information
		UserDto userDto = new UserDto();
		userDto.setId(existingUser.getId());
		userDto.setEmail(existingUser.getEmail());
		userDto.setFirstName(existingUser.getFirstName());
		userDto.setLastName(existingUser.getLastName());
		userDto.setDisplayName(existingUser.getFirstName() + " " + existingUser.getLastName());
		userTokenDto.setUser(userDto);
		// Create claims for JWT body
		Map<String, Object> claims = new HashMap<>();
		claims.put("email", existingUser.getEmail());
		claims.put("userId", existingUser.getId());
		claims.put("lastName", existingUser.getLastName());
		claims.put("firstName", existingUser.getFirstName());
		// Create token expires in 1 hour
		String token = JwtUtil.createToken(claims, 1);
		userTokenDto.setToken(token);
		// Create refresh token expires in 30 days
		String refreshToken = JwtUtil.createToken(claims, 30 * 24);
		userTokenDto.setRefreshToken(refreshToken);
		
		/*  Save refresh token in DB */
		Tokens tokenEntity = new Tokens();
		tokenEntity.setUserId(existingUser.getId());
		tokenEntity.setRefreshToken(refreshToken);
		// Create expires_in field
		// Calculated by seconds of 30 days
		String expiresIn = "" + 30 * 24 * 60 *60;
		tokenEntity.setExpiresIn(expiresIn);
		tokenDao.save(tokenEntity);
		return userTokenDto;
	}

}
