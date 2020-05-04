package com.usac.ayd1.practica3.payload;

import java.util.List;
import java.util.stream.Collectors;

import com.usac.ayd1.practica3.entity.Role;

public class UserSummaryResponse {

	private Long id;
	private String name;
	private String username;
	private String accountNumber;
	private Double balance;
	private List<CreditResponse> credits;
	private List<TransactionResponse> transactions;
	private List<String> roles;

	public UserSummaryResponse(Long id, String name, String username, String accountNumber, Double balance,
			List<CreditResponse> credits, List<TransactionResponse> transactions, List<Role> roles) {
		this.id = id;
		this.name = name;
		this.username = username;
		this.accountNumber = accountNumber;
		this.balance = balance;
		this.credits = credits;
		this.transactions = transactions;
		this.setRoles(roles.stream().map(r -> r.getName().toString()).collect(Collectors.toList()));
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public Double getBalance() {
		return balance;
	}

	public void setBalance(Double balance) {
		this.balance = balance;
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

	public List<String> getRoles() {
		return roles;
	}

	public void setRoles(List<String> roles) {
		this.roles = roles;
	}

}
