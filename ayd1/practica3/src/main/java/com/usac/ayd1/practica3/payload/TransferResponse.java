package com.usac.ayd1.practica3.payload;

public class TransferResponse {

	private Double newBalance;

	public TransferResponse(Double newBalance) {
		this.newBalance = newBalance;
	}

	public Double getNewBalance() {
		return newBalance;
	}

	public void setNewBalance(Double newBalance) {
		this.newBalance = newBalance;
	}

}
