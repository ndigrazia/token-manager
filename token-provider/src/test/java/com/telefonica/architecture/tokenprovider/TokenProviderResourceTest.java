package com.telefonica.architecture.tokenprovider;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

@QuarkusTest
public class TokenProviderResourceTest {

    @Test
    public void testTokenEndpoint() {
        given()
          .when().contentType("application/json")
          .post("/token")
          .then()
             .statusCode(201);
    }
    
}