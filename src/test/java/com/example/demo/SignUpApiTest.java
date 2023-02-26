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
public class SignUpApiTest {

	@Autowired
	private MockMvc mvc;

	@MockBean
	private UsersService usersService;

	@MockBean
	private TokenService tokenService;

	/**
	 * Test signUp api in case email format is invalid
	 * 
	 * @throws Exception
	 */
	@Test
	public void testSignUp_whenEmailFormatNotCorrect() throws Exception {
		performApi("user6", "12345678", "AAA", "Nguyen");
	}

	/**
	 * Test signUp api in case password isn't between 8 and 20 characters
	 * 
	 * @throws Exception
	 */
	@Test
	public void testSignUp_whenPasswordFormatNotCorrect() throws Exception {
		performApi("user10@email.com", "1234567", "AAA", "Nguyen");
	}

	/**
	 * Test signUp api in case email is available in user table
	 * 
	 * @throws Exception
	 */
	@Test
	public void testSignUp_whenEmailIsExist() throws Exception {
		performApi("user6@email.com", "12345678", "AAA", "Nguyen");
	}

	/**
	 * Call signUp api with mock input
	 * 
	 * @param email
	 * @param password
	 * @param firstName
	 * @param lastName
	 * @throws Exception
	 */
	private void performApi(String email, String password, String firstName, String lastName) throws Exception {
		mvc.perform(post("/sign-up").param("email", email).param("password", password).param("firstName", firstName)
				.param("lastName", lastName).contentType(MediaType.MULTIPART_FORM_DATA)
				.accept(MediaType.APPLICATION_FORM_URLENCODED)).andExpect(status().isBadRequest());
	}
}
