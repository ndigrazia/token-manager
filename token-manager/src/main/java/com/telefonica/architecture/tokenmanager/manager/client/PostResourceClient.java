package com.telefonica.architecture.tokenmanager.manager.client;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Singleton;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;

@Singleton
public class PostResourceClient {
    
    private Client client;

    @PostConstruct
    public void createThreadPool() {
        ExecutorService executorService = Executors.newCachedThreadPool();
        client = ClientBuilder.newBuilder().executorService(executorService).build();
	}

    public Token getToken() {
        Token token = client.target("http://demo2946827.mockable.io").path("token")
            .request(MediaType.APPLICATION_JSON_TYPE).post(
                Entity.entity(new Form(), MediaType.APPLICATION_JSON), Token.class);
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