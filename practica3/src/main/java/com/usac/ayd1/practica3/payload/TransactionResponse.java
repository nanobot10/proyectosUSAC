package com.usac.ayd1.practica3.payload;

import java.time.Instant;

import com.usac.ayd1.practica3.entity.Transaction;
import com.usac.ayd1.practica3.enums.TransactionType;

public class TransactionResponse {

	private Long id;
	private TransactionType transactionType;
	private String accountNumber;
	private Instant createdAt;
	private Double amount;
	private String description;

	public TransactionResponse(Long id, TransactionType transactionType, String accountNumber, Instant createdAt,
			Double amount, String description) {
		this.id = id;
		this.transactionType = transactionType;
		this.accountNumber = accountNumber;
		this.createdAt = createdAt;
		this.amount = amount;
		this.description = description;
	}

	public TransactionResponse(Transaction transaction) {
		this.id = transaction.getId();
		this.transactionType = transaction.getTransactionType();
		this.accountNumber = transaction.getAccountNumber();
		this.createdAt = transaction.getCreatedAt();
		this.amount = transaction.getAmount();
		this.description = transaction.getDescription();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public TransactionType getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(TransactionType transactionType) {
		this.transactionType = transactionType;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public Instant getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Instant createdAt) {
		this.createdAt = createdAt;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
