package com.telefonica.architecture.tokenmanager.redis;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@ApplicationScoped
public class RedisClient {

    private JedisPoolConfig config;
    private JedisPool pool;

    @ConfigProperty(name = "redis.host") 
    String host;
    
    @ConfigProperty(name = "redis.http.port") 
    String port;

    public RedisClient() {
		this(new JedisPoolConfig());
	}

    public RedisClient(JedisPoolConfig config) {
        this.config = config;
    }

    @PostConstruct    
    public void openPool() {
        if(pool == null) {
            pool = new JedisPool(config, host, Integer.valueOf(port));
        }
    }

    @PreDestroy
    public void closePool() {
        if(pool != null)  { 
            pool.close();
            pool = null;
        }
    }

    public Jedis getConnection() {
        return pool.getResource();
    }

    public void destroyConnection(Jedis redis) {
        if(redis != null) 
            redis.close();
    }

    public void returnConnection(Jedis redis) {
        if (redis != null)
            pool.returnResource(redis);
    }

	public JedisPoolConfig getConfig() {
		return config;
	}

	public void setConfig(JedisPoolConfig config) {
		this.config = config;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}
     
}