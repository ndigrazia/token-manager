package com.telefonica.architecture.tokenmanager.manager;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;

import java.util.Date;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.telefonica.architecture.tokenmanager.manager.client.PostResourceClient;
import com.telefonica.architecture.tokenmanager.manager.exception.TokenException;
import com.telefonica.architecture.tokenmanager.manager.model.Token;
import com.telefonica.architecture.tokenmanager.manager.str.TokenStr;
import com.telefonica.architecture.tokenmanager.redis.RedisClient;

@Path("/token")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TokenManagerResource {

    @Inject    
    RedisClient  redis;

    @Inject
    PostResourceClient client;

    @GET
    public Token getToken(@QueryParam("provider") String provider) {
        try {
            return TokenStr.getToken(redis, (con)->{
                Token token = new Token();
            
                Date currentDate = new Date();
                token.setDate(currentDate);          
               //String value = con.get(provider);
                token.setToken(client.getToken().getId());
           
                return token;
            });
        } catch (TokenException e) {
            throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    @POST
    public Token postToken(@HeaderParam("provider") String provider) {
        try {
            return TokenStr.getToken(redis, (con)->{
                Token token = new Token();
            
                String value = "algun valor";
                int seconds = 1000;

                con.set(provider, value);
                con.expire(provider, seconds);

                Date currentDate = new Date();
                token.setDate(currentDate);   
                token.setToken(value);

                return token;
            });
        } catch (TokenException e) {
            throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

}