package com.telefonica.architecture.tokenprovider;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

@QuarkusTest
public class TokenProviderResourceTest {

    @Test
    public void testHelloEndpoint() {
        given()
          .when().get("/token")
          .then()
             .statusCode(200);
    }

}