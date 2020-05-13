package com.usac.ayd1.practica3.payload;

import javax.validation.constraints.NotNull;

public class TransferRequest {

	@NotNull
	private String accountNumber;
	@NotNull
	private Double amount;

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

}
