package com.usac.ayd1.practica3.payload;

public class SignUpResponse {

	private Integer userCode;

	public SignUpResponse(Integer userCode) {
		this.userCode = userCode;
	}

	public Integer getUserCode() {
		return userCode;
	}

	public void setUserCode(Integer userCode) {
		this.userCode = userCode;
	}

}
