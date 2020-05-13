package com.usac.ayd1.practica3.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.usac.ayd1.practica3.payload.ApiResponse;
import com.usac.ayd1.practica3.payload.LoginRequest;
import com.usac.ayd1.practica3.payload.SignUpRequest;
import com.usac.ayd1.practica3.service.UserSecurityService;
import com.usac.ayd1.practica3.util.AppConstants;

@RestController
@RequestMapping(AppConstants.BASE_URL + "/auth")
public class AuthController {

	@Autowired
	private UserSecurityService userSecurityService;

	@PostMapping("/signin")
	public ResponseEntity<ApiResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
		return ResponseEntity.ok(userSecurityService.signin(loginRequest));
	}

	@PostMapping("/signup")
	public ResponseEntity<ApiResponse> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
		return ResponseEntity.ok(userSecurityService.signUp(signUpRequest));
	}
}