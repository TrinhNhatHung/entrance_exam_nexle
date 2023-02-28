package com.example.demo.api;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.TokenDto;
import com.example.demo.dto.UserDto;
import com.example.demo.dto.UserTokenDto;
import com.example.demo.entity.Users;
import com.example.demo.exception.EmailExistException;
import com.example.demo.exception.RefreshTokenNotFoundException;
import com.example.demo.service.TokenService;
import com.example.demo.service.UsersService;
import com.example.demo.util.JwtUtil;
import com.example.demo.util.ValidationUtil;

@RestController
@CrossOrigin
public class AuthenticatingApi {

	@Autowired
	private UsersService usersService;

	@Autowired
	private TokenService tokenService;

	/**
	 * API to sign up new account
	 * 
	 * @param user
	 * @return object include inserted user information
	 */
	@PostMapping(path = "/sign-up")
	public ResponseEntity<?> signUp(@RequestBody Users user) {
		try {
			// Check validate email and password
			if (!ValidationUtil.validateEmail(user.getEmail())
					|| !ValidationUtil.validatePassword(user.getPassword())) {
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}
			// Save new user
			UserDto userDto = usersService.save(user);
			return new ResponseEntity<>(userDto, HttpStatus.CREATED);
		} catch (EmailExistException e) {
			// if email is exist
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			// if there is any error
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * API to sign in
	 * 
	 * @param user
	 * @return object include user information, token and refresh token
	 */
	@PostMapping(path = "/sign-in")
	public ResponseEntity<?> signIn(@RequestBody Users user) {
		try {
			// Check validate email and password
			if (!ValidationUtil.validateEmail(user.getEmail())
					|| !ValidationUtil.validatePassword(user.getPassword())) {
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}
			// Sign in
			UserTokenDto tokenDto = usersService.signIn(user);
			return new ResponseEntity<>(tokenDto, HttpStatus.OK);
		} catch (Exception e) {
			// if there is any error
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * API to sign out
	 * 
	 * @param httpRequest
	 * @return
	 */
	@PostMapping(path = "/sign-out")
	public ResponseEntity<?> signOut(HttpServletRequest httpRequest) {
		try {
			// Get Authorization string from header of http request
			String authorization = httpRequest.getHeader("Authorization");
			// Remove "bearer" in begin to get JWT
			String token = authorization.substring(7);
			// Get userId in JWT body
			Integer userId = (Integer) JwtUtil.extractClaim(token, t -> t.get("userId"));
			// Delete refresh token by user_id
			tokenService.deleteByUserId(userId);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			// if there is any error
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * API to get new token and new refreshToken
	 * @param refreshToken
	 * @return object include token and refreshToken
	 */
	@PostMapping(path = "/refresh-token")
	public ResponseEntity<?> refreshToken(@RequestBody Map<String, Object> payload) {
		try {
			String refreshToken = (String) payload.get("refreshToken");
			// Get new token and new refreshToken
			TokenDto tokenDto = tokenService.refreshToken(refreshToken);
			return new ResponseEntity<>(tokenDto, HttpStatus.OK);
		} catch (RefreshTokenNotFoundException e) {
			// if refreshToken isn't exist
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			// if there is any error
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
