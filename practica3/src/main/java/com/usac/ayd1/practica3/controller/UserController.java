package com.usac.ayd1.practica3.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.usac.ayd1.practica3.payload.ApiResponse;
import com.usac.ayd1.practica3.payload.CreditRequest;
import com.usac.ayd1.practica3.payload.TransferRequest;
import com.usac.ayd1.practica3.service.UserService;
import com.usac.ayd1.practica3.util.AppConstants;

@RestController
@RequestMapping(AppConstants.BASE_URL + "/users")
public class UserController {

	@Autowired
	private UserService userService;

	@GetMapping("/balance")
	public ResponseEntity<ApiResponse> balance() {
		return ResponseEntity.ok(userService.getUserBalance());
	}

	@PostMapping("/transfer")
	public ResponseEntity<ApiResponse> transfer(@Valid @RequestBody TransferRequest transferRequest) {
		return ResponseEntity.ok(userService.makeTransfer(transferRequest));
	}

	@PostMapping("/credit")
	public ResponseEntity<ApiResponse> credit(@Valid @RequestBody CreditRequest creditRequest) {
		return ResponseEntity.ok(userService.applyForCredit(creditRequest));
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@DeleteMapping("/{id}")
	public ResponseEntity<ApiResponse> deleteUser(@PathVariable(name = "id") Long id) {
		return ResponseEntity.ok(userService.delete(id));
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping
	public ResponseEntity<ApiResponse> getAllUsersByAccountNumber(
			@RequestParam(name = "accountNumber", required = false) String accountNumber) {
		return ResponseEntity.ok(userService.getAllUsers(accountNumber));
	}

	@GetMapping("/user-profile")
	public ResponseEntity<ApiResponse> getUser() {
		return ResponseEntity.ok(userService.getUserProfile());
	}

	@GetMapping("/user-summary")
	public ResponseEntity<ApiResponse> getUserSummary() {
		return ResponseEntity.ok(userService.getUserSummary());
	}

}
