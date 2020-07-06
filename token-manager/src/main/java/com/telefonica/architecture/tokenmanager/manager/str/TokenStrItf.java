package com.telefonica.architecture.tokenmanager.manager.str;

import com.telefonica.architecture.tokenmanager.manager.model.Token;

import redis.clients.jedis.Jedis;

public interface TokenStrItf {

    public Token get(Jedis con); 

}