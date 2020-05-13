package com.usac.ayd1.practica3.payload;

import javax.validation.constraints.NotNull;

public class CreditRequest {

	@NotNull
	private Double amount;
	@NotNull
	private String description;

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
