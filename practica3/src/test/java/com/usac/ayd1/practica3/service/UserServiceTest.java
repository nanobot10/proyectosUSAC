package com.usac.ayd1.practica3.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import com.usac.ayd1.practica3.entity.Role;
import com.usac.ayd1.practica3.enums.RoleName;
import com.usac.ayd1.practica3.payload.ApiResponse;
import com.usac.ayd1.practica3.payload.SignUpRequest;
import com.usac.ayd1.practica3.payload.SignUpResponse;
import com.usac.ayd1.practica3.repository.AccountRepository;
import com.usac.ayd1.practica3.repository.RoleRepository;
import com.usac.ayd1.practica3.repository.UserRepository;
import com.usac.ayd1.practica3.security.JwtTokenProvider;

@RunWith(SpringRunner.class)
@DataJpaTest
public class UserServiceTest {

	@InjectMocks
	private UserSecurityService userSecurityService;

	@Mock
	private AuthenticationManager authenticationManager;

	@Mock
	private UserRepository userRepository;

	@Mock
	private RoleRepository roleRepository;

	@Mock
	private PasswordEncoder passwordEncoder;

	@Mock
	private AccountRepository accountRepository;

	@Mock
	private JwtTokenProvider tokenProvider;

	@Mock
	private TransactionService transactionService;

	@Test
	public void signUp() {

		SignUpRequest request = createBasicRequest();

		when(userRepository.existsByUsername(request.getUsername())).thenReturn(Boolean.FALSE);
		when(roleRepository.findByName(RoleName.ROLE_USER)).thenReturn(Optional.of(new Role(RoleName.ROLE_USER)));
		when(userRepository.getMaxUserCode()).thenReturn(null);
		ApiResponse response = userSecurityService.signUp(request);

		assertEquals(Boolean.TRUE, response.getSuccess());
		assertEquals(Integer.valueOf(1001), ((SignUpResponse) response.getData()).getUserCode());

	}

	@Test
	public void signUpUsernameAlreadyExists() {
		SignUpRequest request = createBasicRequest();
		when(userRepository.existsByUsername(request.getUsername())).thenReturn(Boolean.TRUE);
		ApiResponse response = userSecurityService.signUp(request);
		assertEquals(Boolean.FALSE, response.getSuccess());
		assertEquals("Username is already taken!", response.getMessage());
	}

	@Test
	public void signUpInvalidUsernameRegex() {
		SignUpRequest request = createBasicRequest();
		when(userRepository.existsByUsername(request.getUsername())).thenReturn(Boolean.FALSE);
		request.setUsername("usernameTest");
		ApiResponse response = userSecurityService.signUp(request);
		assertEquals(Boolean.FALSE, response.getSuccess());
		assertEquals("Username does not meet the restrictions", response.getMessage());
	}
	
	@Test
	public void signUpInvalidUsernameLength() {
		SignUpRequest request = createBasicRequest();
		when(userRepository.existsByUsername(request.getUsername())).thenReturn(Boolean.FALSE);
		request.setUsername("usernameTest1");
		ApiResponse response = userSecurityService.signUp(request);
		assertEquals(Boolean.FALSE, response.getSuccess());
		assertEquals("Username does not meet the restrictions", response.getMessage());
	}

	@Test
	public void signUpInvalidPassword() {
		SignUpRequest request = createBasicRequest();
		when(userRepository.existsByUsername(request.getUsername())).thenReturn(Boolean.FALSE);
		request.setPassword("password");
		ApiResponse response = userSecurityService.signUp(request);
		assertEquals(Boolean.FALSE, response.getSuccess());
		assertEquals("Password does not meet the restrictions", response.getMessage());
	}

	private SignUpRequest createBasicRequest() {
		SignUpRequest request = new SignUpRequest();
		request.setName("Test User");
		request.setUsername("testuser1");
		request.setEmail("test@example.com");
		request.setPassword("holaHola10!");
		return request;
	}

}
