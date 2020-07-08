package com.telefonica.architecture.tokenmanager.manager.config;

import java.util.HashMap;
import java.util.Map;

import com.telefonica.architecture.tokenmanager.redis.RedisClient;

import org.eclipse.microprofile.config.spi.ConfigSource;

import redis.clients.jedis.Jedis;

public class EnviromentConfigSource implements ConfigSource {

	private static final String REDIS_HOST_ENV = "REDIS_HOST";
	private static final String REDIS_PORT_ENV = "REDIS_HTTP_PORT";

	private Map<String, String> properties = new HashMap<String, String>();

	public EnviromentConfigSource() {
		init();
	}

	private void init() {
		loadEnvProperties();
		loadRedisProperties();
	}
	
	private void loadEnvProperties() {
		String host = (
			System.getProperty(REDIS_HOST_ENV)!=null?System.getProperty(REDIS_HOST_ENV):
				(System.getenv(REDIS_HOST_ENV)!=null?System.getenv(REDIS_HOST_ENV):
					"redis-token-manager.192.168.99.100.nip.io"));
		properties.put("redis.host", host);

		String port = (
					System.getProperty(REDIS_PORT_ENV)!=null?System.getProperty(REDIS_PORT_ENV):
						(System.getenv(REDIS_PORT_ENV)!=null?System.getenv(REDIS_PORT_ENV):"30026"));
		properties.put("redis.http.port", port);
	}
	
	private void loadRedisProperties() {
		RedisClient redis = new RedisClient();

		Jedis con = null;

        try {
			
			redis.setHost(properties.get("redis.host"));
			redis.setPort(properties.get("redis.http.port"));

			redis.openPool();

			con = redis.getConnection();

			//Loading properties...
			for(String key:con.keys("providers*")) 
				properties.put(key.replace(":", ".").concat(".url"), con.get(key));

        } catch (Exception e) { 
            throw new RuntimeException(e);
        } finally {
            if (con != null) {
                try {
					redis.returnConnection(con);
					redis.closePool();
					redis = null;
                    con = null;
                } catch (Exception e) { 
                   throw new RuntimeException(e);
                }
            }
		}
	}

	@Override
	public String getName() {
        return EnviromentConfigSource.class.getSimpleName();	
	}

	@Override
	public Map<String, String> getProperties() {
		return properties;
	}

	//@Override
	public String getValue(String propertyName) {
		return properties.get(propertyName);
	}

	@Override
	public int getOrdinal() {
		return 900;
	}
    
}