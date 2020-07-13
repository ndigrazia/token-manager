package com.telefonica.architecture.tokenmanager.manager.model;

import java.util.Date;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class TokenResponse {

	private String type;
    private String token;
    private Date date;

	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
    
    public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}