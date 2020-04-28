package com.usac.ayd1.practica3.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.usac.ayd1.practica3.payload.ApiResponse;
import com.usac.ayd1.practica3.payload.DebitRequest;
import com.usac.ayd1.practica3.service.DebitService;
import com.usac.ayd1.practica3.util.AppConstants;

@RestController
@RequestMapping(AppConstants.BASE_URL + "/debits")
public class DebitController {

	@Autowired
	private DebitService debitService;

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PutMapping
	public ResponseEntity<ApiResponse> debit(@Valid @RequestBody DebitRequest debitRequest) {
		return ResponseEntity.ok(debitService.makeDebit(debitRequest));
	}

}
