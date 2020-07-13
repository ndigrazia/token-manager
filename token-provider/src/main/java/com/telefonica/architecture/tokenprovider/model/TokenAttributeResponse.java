package com.telefonica.architecture.tokenprovider.model;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class TokenAttributeResponse {

    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }  
        
}