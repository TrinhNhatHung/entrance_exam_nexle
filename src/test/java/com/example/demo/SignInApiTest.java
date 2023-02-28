package com.example.demo;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.example.demo.api.AuthenticatingApi;
import com.example.demo.service.TokenService;
import com.example.demo.service.UsersService;

@WebMvcTest(AuthenticatingApi.class)
public class SignInApiTest {

	@Autowired
	private MockMvc mvc;

	@MockBean
	private UsersService usersService;

	@MockBean
	private TokenService tokenService;

	/**
	 * Test signIn api in case email format is invalid
	 * 
	 * @throws Exception
	 */
	@Test
	public void testSignIn_whenEmailFormatNotCorrect() throws Exception {
		performApi("user6", "12345678");
	}

	/**
	 * Test signIn api in case password isn't between 8 and 20 characters
	 * 
	 * @throws Exception
	 */
	@Test
	public void testSignUp_whenPasswordFormatNotCorrect() throws Exception {
		performApi("user10@email.com", "1234567");
	}

	/**
	 * Call signIn api with mock input
	 * 
	 * @param email
	 * @param password
	 * @throws Exception
	 */
	private void performApi(String email, String password) throws Exception {
		mvc.perform(post("/sign-in").param("email", email).param("password", password)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());
	}
}
