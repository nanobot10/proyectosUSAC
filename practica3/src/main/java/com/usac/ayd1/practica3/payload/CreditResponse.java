package com.usac.ayd1.practica3.payload;

import java.time.Instant;

import com.usac.ayd1.practica3.enums.Status;

public class CreditResponse {

	private Long id;
	private Instant createdAt;
	private String accountNumber;
	private Double amount;
	private Status status;

	public CreditResponse(Long id, Status status, String accountNumber, Instant createdAt, Double amount) {
		this.id = id;
		this.status = status;
		this.accountNumber = accountNumber;
		this.createdAt = createdAt;
		this.amount = amount;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Instant getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Instant createdAt) {
		this.createdAt = createdAt;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

}
