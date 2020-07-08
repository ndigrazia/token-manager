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
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.HttpHeaders;

import com.telefonica.architecture.tokenmanager.manager.client.ResourceProviderClient;
import com.telefonica.architecture.tokenmanager.manager.client.TokenProvider;
import com.telefonica.architecture.tokenmanager.manager.exception.TokenException;
import com.telefonica.architecture.tokenmanager.manager.model.TokenResponse;
import com.telefonica.architecture.tokenmanager.manager.str.TokenResponseStr;
import com.telefonica.architecture.tokenmanager.redis.RedisClient;

import redis.clients.jedis.Jedis;

@Path("/token")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TokenManagerResource {

    @Inject    
    private RedisClient  redis;

    @Inject
    private ResourceProviderClient client;

    @GET
    public TokenResponse getToken(@Context final HttpHeaders headers, @QueryParam("provider") final String provider) {
        try {
                TokenResponseStr str = new TokenResponseStr();
                TokenResponse token = str.getToken(redis, (con)->{
                String key = "tokens:"+ provider;
                
                String value = con.get(key);
                if(value == null)
                    value = loadTokenFromProvider(provider, con);
                
                return createResponse(value);
            });
            return token;
        } catch (final TokenException e) {
            e.printStackTrace();
            throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    private String loadTokenFromProvider(String provider, Jedis con) throws TokenException {
        TokenProvider token = client.getToken(provider);
        
        String json = token.attributesToJson();

        String key = "tokens:"+ provider;

        con.set(key, json);
        con.expire(key, token.getExpires_in());

        return json;
    }

    private TokenResponse createResponse(String value) {
        final TokenResponse token = new TokenResponse();

        token.setDate(new Date());   
        token.setToken(value);
       
        return token;
    }

    @POST
    public TokenResponse postToken(@Context final HttpHeaders headers, @HeaderParam("provider") final String provider) {
        try {
            TokenResponseStr str = new TokenResponseStr();
            TokenResponse token = str.getToken(redis, (con)->{
                return createResponse(loadTokenFromProvider(provider, con));
            });
            return token;
        } catch (final TokenException e) {
            e.printStackTrace();
            throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

}