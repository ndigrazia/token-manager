package com.telefonica.architecture.tokenprovider.model;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class TokenResponse {

    private String token_type;
    private int expires_in;
    private TokenAttributeResponse attribute;

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

    public TokenAttributeResponse getAttribute() {
        return attribute;
    }

    public void setAttribute(TokenAttributeResponse attribute) {
        this.attribute = attribute;
    }
    
}