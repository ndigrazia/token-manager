package com.telefonica.architecture.tokenmanager.manager.health;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.telefonica.architecture.tokenmanager.manager.exception.TokenException;
import com.telefonica.architecture.tokenmanager.manager.model.Token;
import com.telefonica.architecture.tokenmanager.manager.str.TokenStr;
import com.telefonica.architecture.tokenmanager.redis.RedisClient;

import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.HealthCheckResponseBuilder;
import org.eclipse.microprofile.health.Readiness;

@Readiness
@ApplicationScoped
public class ReadinessHealthCheck implements HealthCheck {

    private static final String PONG = "PONG";

    @Inject    
    RedisClient redis;
    
    @Override
    public HealthCheckResponse call() {
        HealthCheckResponseBuilder responseBuilder = HealthCheckResponse.named("Redis connection health check");

        try {
            TokenStr.getToken(redis, (con)->{
                if (con.ping().equalsIgnoreCase(PONG)) {
                    responseBuilder.up();
                } else {
                    responseBuilder.down();
                }

                return new Token();
            });
        } catch (TokenException e) {
            responseBuilder.down();
        }
        
        return responseBuilder.build();
    }   
    
}