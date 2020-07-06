package com.telefonica.architecture.tokenmanager.manager.client;

public class Token {
    
    private String token_type;
    private int expires_in;
    private String id;
    
	public String getToken_type() {
		return token_type;
	}
	public void setToken_type(String token_type) {
		this.token_type = token_type;
	}
	public int getExpires_in() {
		return expires_in;
	}
	public void setExpires_in(int expires_in) {
		this.expires_in = expires_in;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

}