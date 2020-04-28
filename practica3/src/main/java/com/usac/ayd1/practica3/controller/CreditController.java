package com.usac.ayd1.practica3.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.usac.ayd1.practica3.enums.Status;
import com.usac.ayd1.practica3.payload.ApiResponse;
import com.usac.ayd1.practica3.service.CreditService;
import com.usac.ayd1.practica3.util.AppConstants;

@RestController
@RequestMapping(AppConstants.BASE_URL + "/credits")
public class CreditController {

	@Autowired
	private CreditService creditService;

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping
	public ResponseEntity<ApiResponse> allCredits() {
		return ResponseEntity.ok(creditService.getAllCredits());
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/{id}")
	public ResponseEntity<ApiResponse> getCredit(@PathVariable(name = "id") Long id) {
		return ResponseEntity.ok(creditService.getCredit(id));
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PutMapping("/{id}/{status}")
	public ResponseEntity<ApiResponse> changeCreditStatus(@PathVariable(name = "id") Long id,
			@PathVariable(name = "status") Status status) {
		return ResponseEntity.ok(creditService.changeCreditStatus(id, status));
	}

}
