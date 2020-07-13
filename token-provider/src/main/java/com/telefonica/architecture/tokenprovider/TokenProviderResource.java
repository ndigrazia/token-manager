package com.telefonica.architecture.tokenprovider;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.telefonica.architecture.tokenprovider.model.TokenAttributeResponse;
import com.telefonica.architecture.tokenprovider.model.TokenResponse;

@Path("/token")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TokenProviderResource {

    @GET
    public TokenResponse getToken() {
        final int HOUR = 1440;

        TokenAttributeResponse att = new TokenAttributeResponse();
        att.setId("d5f5c0393381c342c6aa7b907b4eaf9e95e6652aec41ac6d1926fe9563a08a7a");

        TokenResponse res = new TokenResponse();
        res.setToken_type("Bearer");
        res.setExpires_in(HOUR);
        res.setAttribute(att);

        return res;
    }

}