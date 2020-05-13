package com.usac.ayd1.practica3.service;

import java.util.Collections;
import java.util.UUID;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import com.usac.ayd1.practica3.entity.Account;
import com.usac.ayd1.practica3.entity.Role;
import com.usac.ayd1.practica3.entity.User;
import com.usac.ayd1.practica3.enums.RoleName;
import com.usac.ayd1.practica3.enums.TransactionType;
import com.usac.ayd1.practica3.exception.AppException;
import com.usac.ayd1.practica3.payload.ApiResponse;
import com.usac.ayd1.practica3.payload.JwtAuthenticationResponse;
import com.usac.ayd1.practica3.payload.LoginRequest;
import com.usac.ayd1.practica3.payload.SignUpRequest;
import com.usac.ayd1.practica3.payload.SignUpResponse;
import com.usac.ayd1.practica3.repository.AccountRepository;
import com.usac.ayd1.practica3.repository.RoleRepository;
import com.usac.ayd1.practica3.repository.UserRepository;
import com.usac.ayd1.practica3.security.JwtTokenProvider;
import com.usac.ayd1.practica3.security.UserPrincipal;

@Service
public class UserSecurityService {

	private static final Logger log = LoggerFactory.getLogger(UserSecurityService.class);

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private JwtTokenProvider tokenProvider;

	@Autowired
	private TransactionService transactionService;

	public ApiResponse signin(LoginRequest loginRequest) {
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

		UserPrincipal user = (UserPrincipal) authentication.getPrincipal();
		if (!ObjectUtils.nullSafeEquals(user.getUserCode(), loginRequest.getUserCode())) {
			return new ApiResponse(false, "Invalid user code for username provided");
		}

		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = tokenProvider.generateToken(authentication);
		return new ApiResponse(true, "success", new JwtAuthenticationResponse(jwt));
	}

	public ApiResponse signUp(SignUpRequest signUpRequest) {
		
		if (Boolean.TRUE.equals(userRepository.existsByUsername(signUpRequest.getUsername()))) {
			return new ApiResponse(false, "Username is already taken!");
		}
		
		if (Boolean.TRUE.equals(userRepository.existsByEmail(signUpRequest.getEmail()))) {
			return new ApiResponse(false, "Email is already taken!");
		}
		

		if (!validateUsername(signUpRequest.getUsername())) {
			return new ApiResponse(false, "Username does not meet the restrictions");
		}

		User user = new User(signUpRequest.getName(), signUpRequest.getUsername(), getMaxUserCode(),
				signUpRequest.getEmail(), signUpRequest.getPassword());

		if (!validatePassword(user.getPassword())) {
			return new ApiResponse(false, "Password does not meet the restrictions");
		}

		user.setPassword(passwordEncoder.encode(user.getPassword()));

		Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
				.orElseThrow(() -> new AppException("User Role not set."));

		user.setRoles(Collections.singleton(userRole));

		userRepository.saveAndFlush(user);
		Account account = new Account();
		account.setAccountNumber(UUID.randomUUID().toString());
		account.setBalance(Double.valueOf(0.0));
		account.setUser(user);
		accountRepository.save(account);
		transactionService.saveTransaction(transactionService.createTransaction(user, account.getAccountNumber(),
				TransactionType.CREDIT, account.getBalance(), "New Account"));
		return new ApiResponse(true, "User registered successfully", new SignUpResponse(user.getUserCode()));
	}

	private boolean validatePassword(String password) {
		return Pattern.compile("(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[$@$!%*?&])[A-Za-z\\d$@$!%*?&].{8,}")
				.matcher(password).find();
	}

	private boolean validateUsername(String username) {
		if (StringUtils.isEmpty(username)) {
			return false;
		}
		return (username.length() >= 3 && username.length() <= 12)
				&& Pattern.compile("^(?=.*[0-9])(?=.*[a-zA-Z])([a-zA-Z0-9]+)$").matcher(username).find();
	}

	private Integer getMaxUserCode() {
		Integer maxUserCode = userRepository.getMaxUserCode();
		if (maxUserCode == null) {
			return 1000 + 1;
		} else {
			return maxUserCode + 1;
		}
	}

}
