package com.telefonica.architecture.tokenclient.client;

import javax.ws.rs.core.Response;

public interface RequestStr {

    public Response execute(Token token);
    
}