package com.telefonica.architecture.tokenmanager.manager.str;

import com.telefonica.architecture.tokenmanager.manager.exception.TokenException;
import com.telefonica.architecture.tokenmanager.manager.model.TokenResponse;

import redis.clients.jedis.Jedis;

public interface TokenResponseStrItf {

    public TokenResponse get(Jedis con) throws TokenException; 

}