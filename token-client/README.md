# token-client project

## Packaging the application

The application can be packaged using `mvn clean package`.
It produces the `token-client-1.0-SNAPSHOT.jar` file in the `/target` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `target/lib` directory.

## Using the application

You can create a class with the following code:

```java
    public static void main( String[] args) {
        TokenClient requet = new TokenClient();

        try {
            Response resp = requet.execute(
                "http://token-manager-token-manager-demo.192.168.99.100.nip.io/", "ypf", (token)->{
                    Client client = ClientBuilder.newClient();
                    String auth = token.getType().concat(" ").concat(token.headertoJson().getString("id"));
                    return client.target("http://demo2946827.mockable.io/").request(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, auth).get();
            });
            
            System.out.println(resp.readEntity(String.class));
        } catch (TokenClientException e) {
            e.printStackTrace();
        }
    }
```