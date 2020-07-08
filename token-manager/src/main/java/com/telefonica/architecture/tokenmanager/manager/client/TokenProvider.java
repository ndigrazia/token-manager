package com.telefonica.architecture.tokenmanager.manager.client;

import java.util.Arrays;

import com.google.gson.JsonObject;

public class TokenProvider {
    
    private String token_type;
    private int expires_in;
    private Object attributes;
    
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
	public Object getAttributes() {
		return attributes;
	}
	public void setAttributes(Object attributes) {
		this.attributes = attributes;
	}

	public String attributesToJson() {
		JsonObject json = new JsonObject();

		String[] parts = attributes.toString().split(",");
		parts[0] = parts[0].replace("{", "");
		parts[parts.length-1] = parts[parts.length-1].replace("}", "");
		for(String line : Arrays.asList(parts)) {
			String[] split = line.split("=");
			json.addProperty(split[0], split[1]);
		}
		
		return json.toString();
	}

}	