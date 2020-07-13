package com.telefonica.architecture.tokenmanager.manager.client;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.JsonObject;

public class TokenProvider {
	
	public static final String TOKEN_TYPE = "token_type";
	public static final String TOKEN_VALUE = "value";

    private String token_type;
    private int expires_in;
    private Object attribute;
    
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
	public Object getAttribute() {
		return attribute;
	}
	public void setAttribute(Object attribute) {
		this.attribute = attribute;
	}

	public String attributeToJson() {
		JsonObject json = new JsonObject();

		String[] parts = attribute.toString().split(",");
		parts[0] = parts[0].replace("{", "");
		parts[parts.length-1] = parts[parts.length-1].replace("}", "");
		for(String line : Arrays.asList(parts)) {
			String[] split = line.split("=");
			json.addProperty(split[0], split[1]);
		}
		
		return json.toString();
	}

	public Map<String, String> toMap() {
		Map<String, String> map = new HashMap<String, String>();
		
		map.put(TOKEN_TYPE, token_type);
		map.put(TOKEN_VALUE, attributeToJson());

		return map;
	}
	
}	