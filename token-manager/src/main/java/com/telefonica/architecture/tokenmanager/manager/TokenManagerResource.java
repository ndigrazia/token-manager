package com.telefonica.architecture.tokenmanager.manager;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;

import java.util.Date;
import java.util.Map;

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
    RedisClient  redis;

    @Inject
    ResourceProviderClient client;

    @GET
    public Response getToken(@Context final HttpHeaders headers, @QueryParam("provider") final String provider) {
        try {
                TokenResponseStr str = new TokenResponseStr();
                TokenResponse token = str.getToken(redis, (con)->{
                
                Map<String, String> value = con.hgetAll(getKeyProvider(provider));

                if(value == null || value.isEmpty())
                    value = loadTokenFromProvider(provider, con);
                
                return createResponse(value);
            });
            return Response.ok(token).status(200).build();
        } catch (final TokenException e) {
            e.printStackTrace();
            throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    private Map<String, String> loadTokenFromProvider(String provider, Jedis con) throws TokenException {
        TokenProvider token = client.getToken(provider);
        
        String key = getKeyProvider(provider);

        Map<String, String> map = token.toMap();

        con.hmset(key, map);
        con.expire(key, token.getExpires_in());

        return map;
    }

    private String getKeyProvider(String provider) {
        return  "tokens:"+ provider;
    }

    private TokenResponse createResponse(Map<String, String> map) {
        final TokenResponse token = new TokenResponse();

        token.setDate(new Date());   
        token.setType(map.get(TokenProvider.TOKEN_TYPE));
        token.setToken(map.get(TokenProvider.TOKEN_VALUE));
       
        return token;
    }

    @POST
    public Response postToken(@Context final HttpHeaders headers, @HeaderParam("provider") final String provider) {
        try {
            TokenResponseStr str = new TokenResponseStr();
            TokenResponse token = str.getToken(redis, (con)->{
                return createResponse(loadTokenFromProvider(provider, con));
            });
            return Response.ok(token).status(201).build();
        } catch (final TokenException e) {
            e.printStackTrace();
            throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

}