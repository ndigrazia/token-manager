package com.telefonica.architecture.tokenclient.client;

import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class TokenClient {
 
    private static final String REST_PATH = "token";
    
    private Client client = ClientBuilder.newClient();

    public Response execute(String uri, String provider, RequestStr req) throws TokenClientException {
        
      Token token = null;
      Response resp = null;

      try { 
        token = client.target(uri).path(
        String.valueOf(REST_PATH)).queryParam("provider", provider).request(MediaType.APPLICATION_JSON)
          .get(Token.class);
        
        resp = req.execute(token);

        if(resp.getStatus()==Response.Status.UNAUTHORIZED.getStatusCode())
          throw new NotAuthorizedException(resp);
      
      } catch (NotAuthorizedException e) {
          try {
            token = client.target(uri).path(String.valueOf("token")).request(MediaType.APPLICATION_JSON)
                    .header("provider", provider).post(Entity.entity(new Form(), MediaType.APPLICATION_JSON),
                      Token.class);
            resp = req.execute(token);
          } catch (Exception e1) {
            throw new TokenClientException(e1); 
          }
      } catch(Exception e){   
        throw new TokenClientException(e); 
      } finally {
        token = null;
      }

      return resp;

    }


}