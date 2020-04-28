package com.usac.ayd1.practica3.payload;

import java.util.List;

public class UserSummaryResponse {

	private Long id;
	private String username;
	private String accountNumber;
	private Integer userCode;
	private List<CreditResponse> credits;
	private List<TransactionResponse> transactions;
	
	

	public UserSummaryResponse(Long id, String username, String accountNumber, Integer userCode,
			List<CreditResponse> credits, List<TransactionResponse> transactions) {
		this.id = id;
		this.username = username;
		this.accountNumber = accountNumber;
		this.userCode = userCode;
		this.credits = credits;
		this.transactions = transactions;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public Integer getUserCode() {
		return userCode;
	}

	public void setUserCode(Integer userCode) {
		this.userCode = userCode;
	}

	public List<CreditResponse> getCredits() {
		return credits;
	}

	public void setCredits(List<CreditResponse> credits) {
		this.credits = credits;
	}

	public List<TransactionResponse> getTransactions() {
		return transactions;
	}

	public void setTransactions(List<TransactionResponse> transactions) {
		this.transactions = transactions;
	}

}
