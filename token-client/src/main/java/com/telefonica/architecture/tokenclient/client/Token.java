package com.telefonica.architecture.tokenclient.client;

import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.json.JSONObject;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Token {

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
	
	public JSONObject headertoJson() {
        return new JSONObject(token);
	}
	
}