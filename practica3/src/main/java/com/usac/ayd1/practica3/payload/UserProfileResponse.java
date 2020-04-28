package com.usac.ayd1.practica3.payload;

import java.util.List;
import java.util.stream.Collectors;

import com.usac.ayd1.practica3.entity.Role;

public class UserProfileResponse {

	private String username;
	private String accountNumber;
	private Integer userCode;
	private List<String> roles;

	public UserProfileResponse(String username, String accountNumber, Integer userCode, List<Role> roles) {
		this.username = username;
		this.accountNumber = accountNumber;
		this.userCode = userCode;
		this.roles = roles.stream().map(r -> r.getName().toString()).collect(Collectors.toList());
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

	public List<String> getRoles() {
		return roles;
	}

	public void setRoles(List<String> roles) {
		this.roles = roles;
	}

}
