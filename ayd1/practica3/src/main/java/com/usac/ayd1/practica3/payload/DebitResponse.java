package com.usac.ayd1.practica3.payload;

public class DebitResponse {

	private Double newBalance;

	public DebitResponse(Double newBalance) {
		this.newBalance = newBalance;
	}

	public Double getNewBalance() {
		return newBalance;
	}

	public void setNewBalance(Double newBalance) {
		this.newBalance = newBalance;
	}

}
