package com.telefonica.architecture.tokenmanager.manager.client;

import java.net.URI;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;

import com.telefonica.architecture.tokenmanager.manager.exception.TokenException;

import org.eclipse.microprofile.config.Config;

@Singleton
public class ResourceProviderClient {
    
    private Client client;

    @Inject
    Config config;

    @PostConstruct
    public void createThreadPool() {
        if(client == null) {
            ExecutorService executorService = Executors.newCachedThreadPool();
            client = ClientBuilder.newBuilder().executorService(executorService).build();
        }
	}

    public TokenProvider getToken(String provider) throws TokenException {
        String url = config.getValue("providers."+provider+".url", String.class);

        if(url==null)
            throw new TokenException("url " + provider + " wasn't loaded.");

        TokenProvider token = client.target(URI.create(url)).path("token")
            .request(MediaType.APPLICATION_JSON_TYPE).post(
                Entity.entity(new Form(), MediaType.APPLICATION_JSON), TokenProvider.class);

         return token;
    }

    @PreDestroy
    public void closeThreadPool() {
        if(client != null) {
            client.close();
            client = null;
        }
    }

}